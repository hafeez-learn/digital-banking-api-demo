package com.banking.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayConfig {

    @Value("${account.service.url:http://localhost:8081}")
    private String accountServiceUrl;

    @Value("${transaction.service.url:http://localhost:8082}")
    private String transactionServiceUrl;

    @Value("${payment.service.url:http://localhost:8083}")
    private String paymentServiceUrl;

    @Bean
    public WebClient accountServiceClient() {
        return WebClient.builder()
            .baseUrl(accountServiceUrl)
            .build();
    }

    @Bean
    public WebClient transactionServiceClient() {
        return WebClient.builder()
            .baseUrl(transactionServiceUrl)
            .build();
    }

    @Bean
    public WebClient paymentServiceClient() {
        return WebClient.builder()
            .baseUrl(paymentServiceUrl)
            .build();
    }
}
