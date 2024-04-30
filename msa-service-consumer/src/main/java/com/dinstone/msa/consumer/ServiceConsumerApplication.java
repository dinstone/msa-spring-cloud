
package com.dinstone.msa.consumer;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import com.dinstone.msa.gray.GrayFeignRequestInterceptor;
import com.dinstone.msa.gray.GrayLoadBalancerClientConfig;
import com.dinstone.msa.gray.GrayRestRequestInterceptor;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan("com.dinstone.msa.apm.endpoint, com.dinstone.msa.consumer")
@LoadBalancerClients(defaultConfiguration = GrayLoadBalancerClientConfig.class)
public class ServiceConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceConsumerApplication.class, args);
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate(GrayRestRequestInterceptor grayRestRequestInterceptor) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Collections.singletonList(grayRestRequestInterceptor));
		return restTemplate;
	}

	@Bean
	GrayRestRequestInterceptor grayRestRequestInterceptor() {
		return new GrayRestRequestInterceptor();
	}

	@Bean
	GrayFeignRequestInterceptor grayFeignRequestInterceptor() {
		return new GrayFeignRequestInterceptor();
	}

}
