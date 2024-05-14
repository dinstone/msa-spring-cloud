package com.dinstone.msa.gateway;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

import java.net.URI;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultRequest;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class GrayLoadBalancerClientFilter extends ReactiveLoadBalancerClientFilter {

	private LoadBalancerClientFactory clientFactory;

	private GatewayLoadBalancerProperties properties;

	public GrayLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory,
			GatewayLoadBalancerProperties properties) {
		super(clientFactory, properties);

		this.clientFactory = clientFactory;
		this.properties = properties;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
		String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
		if (url == null || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
			return chain.filter(exchange);
		}
		// preserve the original url
		addOriginalRequestUrl(exchange, url);

		return choose(exchange).doOnNext(response -> {
			if (!response.hasServer()) {
				throw NotFoundException.create(properties.isUse404(), "Unable to find instance for " + url.getHost());
			}

			URI uri = exchange.getRequest().getURI();
			String overrideScheme = null;
			if (schemePrefix != null) {
				overrideScheme = url.getScheme();
			}

			DelegatingServiceInstance serviceInstance = new DelegatingServiceInstance(response.getServer(),
					overrideScheme);
			URI requestUrl = this.reconstructURI(serviceInstance, uri);

			exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, requestUrl);
		}).then(chain.filter(exchange));

	}

	private Mono<Response<ServiceInstance>> choose(ServerWebExchange exchange) {
		URI uri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
		ReactorLoadBalancer<ServiceInstance> loadBalancer = this.clientFactory.getInstance(uri.getHost(),
				ReactorServiceInstanceLoadBalancer.class);
		if (loadBalancer == null) {
			throw new NotFoundException("No loadbalancer available for " + uri.getHost());
		}

		HttpHeaders headers = exchange.getRequest().getHeaders();
		String grayValue = headers.getFirst(GrayConstant.GRAY_HEADER_LABEL);
		return loadBalancer.choose(new DefaultRequest<String>(grayValue));
	}

}
