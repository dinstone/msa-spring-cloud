package com.dinstone.msa.gateway;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

import reactor.core.publisher.Mono;

public class GatewayRoundRobinLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private static final Log log = LogFactory.getLog(GatewayRoundRobinLoadBalancer.class);

    private final AtomicInteger position = new AtomicInteger(new Random().nextInt(1000));

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    private final String serviceId;

    public GatewayRoundRobinLoadBalancer(String serviceId,
                                         ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceId = serviceId;

        log.info("GatewayRoundRobinLoadBalancer created for service: " + serviceId);
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get().next().map(instances -> {
            String swimlaneValue = (String) request.getContext();
            ServiceInstance instance = getInstance(instances, swimlaneValue);
            return new DefaultResponse(instance);
        });
    }

    private ServiceInstance getInstance(List<ServiceInstance> instances, String swimlaneValue) {
        if (instances.isEmpty()) {
            log.warn("No servers available for service: " + this.serviceId);
            return null;
        }

        int pos = Math.abs(this.position.incrementAndGet());
        List<ServiceInstance> stableList = new LinkedList<>();
        if (swimlaneValue == null || swimlaneValue.isEmpty() || GatewayConstant.SWIMLANE_STABLE.equalsIgnoreCase(swimlaneValue)) {
            for (ServiceInstance server : instances) {
                Map<String, String> metadata = server.getMetadata();
                String data = metadata.get(GatewayConstant.SWIMLANE_METADATA);
                if (data == null || data.isEmpty() || GatewayConstant.SWIMLANE_STABLE.equals(data)) {
                    stableList.add(server);
                }
            }
            return originChoose(stableList, null, pos);
        } else {
            List<ServiceInstance> targetList = new LinkedList<>();
            for (ServiceInstance server : instances) {
                Map<String, String> metadata = server.getMetadata();
                String data = metadata.get(GatewayConstant.SWIMLANE_METADATA);
                if (data == null || data.isEmpty() || GatewayConstant.SWIMLANE_STABLE.equals(data)) {
                    stableList.add(server);
                } else if (swimlaneValue.equalsIgnoreCase(data)) {
                    targetList.add(server);
                }
            }
            return originChoose(targetList, stableList, pos);
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
        if (instances == null || instances.isEmpty()) {
            return null;
        }

        int size = instances.size();
        return instances.get(pos % size);
    }

}
