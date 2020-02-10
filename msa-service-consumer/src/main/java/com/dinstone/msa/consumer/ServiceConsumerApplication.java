
package com.dinstone.msa.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients
@ComponentScan("com.dinstone.msa.apm.endpoint")
public class ServiceConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceConsumerApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    @ConditionalOnMissingBean
//    OnlineEndpoint onlineEndpoint() {
//        return new OnlineEndpoint(true, true);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    OfflineEndpoint offlineEndpoint() {
//        return new OfflineEndpoint(true, true);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    StartEndpoint startEndpoint() {
//        return new StartEndpoint(true, true);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    StopEndpoint stopEndpoint() {
//        return new StopEndpoint(true, true);
//    }
}
