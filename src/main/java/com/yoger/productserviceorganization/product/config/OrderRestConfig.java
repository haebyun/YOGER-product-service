package com.yoger.productserviceorganization.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class OrderRestConfig {
    @Bean
    public RestClient orderServiceRestClient() {
        return RestClient.builder()
                .baseUrl("")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
