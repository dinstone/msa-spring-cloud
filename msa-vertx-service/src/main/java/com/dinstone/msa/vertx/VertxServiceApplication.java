package com.dinstone.msa.vertx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.dinstone.measure.starter.EnableMeasure;
import com.dinstone.vertx.starter.EnableVertxRest;

@SpringBootApplication
@EnableDiscoveryClient
@EnableVertxRest
@EnableMeasure
public class VertxServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VertxServiceApplication.class, args);
	}

}
