package com.baiyi.cratos.eds.security.apirisk.test.generic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .defaultHeader("User-Agent", "Cratos-API-Risk/1.0")
                .defaultHeader("Accept", "application/json");
    }

}