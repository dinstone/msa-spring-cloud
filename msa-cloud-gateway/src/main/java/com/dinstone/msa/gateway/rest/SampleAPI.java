package com.dinstone.msa.gateway.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class SampleAPI {

    @GetMapping("/test")
    public Mono<String> createProduct() {
        return Mono.just("ok~~~");
    }
}
