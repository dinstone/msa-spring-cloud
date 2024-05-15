
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

import com.dinstone.msa.swimlane.SwimlaneFeignRequestInterceptor;
import com.dinstone.msa.swimlane.SwimlaneLoadBalancerClientConfig;
import com.dinstone.msa.swimlane.SwimlaneRestRequestInterceptor;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan("com.dinstone.msa.apm.endpoint, com.dinstone.msa.consumer")
@LoadBalancerClients(defaultConfiguration = SwimlaneLoadBalancerClientConfig.class)
public class ServiceConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceConsumerApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate(SwimlaneRestRequestInterceptor swimlaneRestRequestInterceptor) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(swimlaneRestRequestInterceptor));
        return restTemplate;
    }

    @Bean
    public SwimlaneRestRequestInterceptor grayRestRequestInterceptor() {
        return new SwimlaneRestRequestInterceptor();
    }

    @Bean
    SwimlaneFeignRequestInterceptor grayFeignRequestInterceptor() {
        return new SwimlaneFeignRequestInterceptor();
    }

}
