
package com.dinstone.msa.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        // only actuator nedd authorized, other permit
        http.csrf().disable().authorizeExchange().pathMatchers("/actuator/*").authenticated().anyExchange().permitAll()
            .and().formLogin().and().httpBasic();
        return http.build();
    }

}
