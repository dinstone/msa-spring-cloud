
package com.dinstone.msa.zipkin;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import zipkin.server.ZipkinServer;
import zipkin2.server.internal.ZipkinActuatorImporter;
import zipkin2.server.internal.ZipkinModuleImporter;
import zipkin2.server.internal.banner.ZipkinBanner;

@SpringBootApplication
@EnableDiscoveryClient
public class ZipkinServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ZipkinServer.class).banner(new ZipkinBanner())
            .initializers(new ZipkinModuleImporter(), new ZipkinActuatorImporter())
            .properties(EnableAutoConfiguration.ENABLED_OVERRIDE_PROPERTY + "=true", "spring.config.name=zipkin-server",
                "spring.application.name=zipkin-server")
            .run(args);

    }
}
