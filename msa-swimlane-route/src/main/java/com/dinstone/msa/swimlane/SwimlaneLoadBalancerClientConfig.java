package com.dinstone.msa.swimlane;

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * https://juejin.cn/post/7075699466019274783
 *
 * @author dinstone
 */
public class SwimlaneLoadBalancerClientConfig {

    @Bean
    SwimlaneRoundRobinLoadBalancer reactorServiceInstanceLoadBalancer(Environment environment,
                                                                      LoadBalancerClientFactory loadBalancerClientFactory) {
        String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new SwimlaneRoundRobinLoadBalancer(serviceId,
                loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class));
    }

}
