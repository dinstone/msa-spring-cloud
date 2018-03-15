package com.dinstone.msa.apm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import de.codecentric.boot.admin.config.EnableAdminServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
public class ApmAdminApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApmAdminApplication.class, args);
	}
}
