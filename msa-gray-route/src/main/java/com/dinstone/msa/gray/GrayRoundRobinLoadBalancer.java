package com.dinstone.msa.gray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.reactive.Request;
import org.springframework.cloud.client.loadbalancer.reactive.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import reactor.core.publisher.Mono;

/**
 * @author admin
 */
public class GrayRoundRobinLoadBalancer implements ReactorServiceInstanceLoadBalancer {

	private static final Log log = LogFactory.getLog(GrayRoundRobinLoadBalancer.class);

	private final AtomicInteger position = new AtomicInteger(new Random().nextInt(1000));

	private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

	private String serviceId;

	public GrayRoundRobinLoadBalancer(String serviceId,
			ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
		this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
		this.serviceId = serviceId;

		log.info("GrayRoundRobinLoadBalancer created for service: " + serviceId);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Mono<Response<ServiceInstance>> choose() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		String grayValue = (String) attributes.getRequest().getHeader(GrayConstant.GRAY_LABEL);

		return choose(grayValue);
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public Mono<Response<ServiceInstance>> choose(Request request) {
		String grayValue = (String) request.getContext();

		return choose(grayValue);
	}

	private Mono<Response<ServiceInstance>> choose(String grayValue) {
		ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
				.getIfAvailable(NoopServiceInstanceListSupplier::new);
		return supplier.get().next().map(instances -> {
			ServiceInstance instance = getInstance(instances, grayValue);
			return new DefaultResponse(instance);
		});
	}

	private ServiceInstance getInstance(List<ServiceInstance> instances, String grayValue) {
		if (instances.isEmpty()) {
			log.warn("No servers available for service: " + this.serviceId);
			return null;
		}

		List<ServiceInstance> grayServerList = new ArrayList<>();
		List<ServiceInstance> normalServerList = new ArrayList<>();
		for (ServiceInstance server : instances) {
			String data = server.getMetadata().get(GrayConstant.GRAY_META);
			if (GrayConstant.GRAY_DATA.equals(data)) {
				grayServerList.add(server);
			} else {
				normalServerList.add(server);
			}
		}

		int pos = Math.abs(this.position.incrementAndGet());
		if (GrayConstant.GRAY_VALUE.equals(grayValue)) {
			return originChoose(grayServerList, normalServerList, pos);
		} else {
			return originChoose(normalServerList, grayServerList, pos);
		}

	}

	private ServiceInstance originChoose(List<ServiceInstance> firstList, List<ServiceInstance> secondList, int pos) {
		ServiceInstance instance = choose(firstList, pos);
		if (instance == null) {
			instance = choose(secondList, pos);
		}
		return instance;
	}

	private ServiceInstance choose(List<ServiceInstance> instances, int pos) {
		int size = instances.size();
		if (size > 0) {
			return instances.get(pos % size);
		}
		return null;
	}

}
