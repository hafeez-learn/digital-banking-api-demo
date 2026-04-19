package com.banking.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GatewayController {

    private final WebClient accountServiceClient;
    private final WebClient transactionServiceClient;
    private final WebClient paymentServiceClient;

    public GatewayController(
            WebClient accountServiceClient,
            WebClient transactionServiceClient,
            WebClient paymentServiceClient) {
        this.accountServiceClient = accountServiceClient;
        this.transactionServiceClient = transactionServiceClient;
        this.paymentServiceClient = paymentServiceClient;
    }

    // ==================== Account Service Routes ====================

    @PostMapping("/accounts")
    public Mono<ResponseEntity<?>> createAccount(@RequestBody Map<String, Object> request) {
        return accountServiceClient.post()
            .uri("/api/accounts")
            .bodyValue(request)
            .retrieve()
            .toEntity(Object.class)
            .map(ResponseEntity::ok);
    }

    @GetMapping("/accounts/{accountNumber}")
    public Mono<ResponseEntity<?>> getAccount(@PathVariable String accountNumber) {
        return accountServiceClient.get()
            .uri("/api/accounts/{accNo}", accountNumber)
            .retrieve()
            .toEntity(Object.class)
            .map(ResponseEntity::ok);
    }

    @GetMapping("/accounts/{accountNumber}/balance")
    public Mono<ResponseEntity<?>> getBalance(@PathVariable String accountNumber) {
        return accountServiceClient.get()
            .uri("/api/accounts/{accNo}/balance", accountNumber)
            .retrieve()
            .toEntity(Object.class)
            .map(ResponseEntity::ok);
    }

    @PutMapping("/accounts/{accountNumber}/status")
    public Mono<ResponseEntity<?>> updateAccountStatus(
            @PathVariable String accountNumber,
            @RequestBody Map<String, String> request) {
        return accountServiceClient.put()
            .uri("/api/accounts/{accNo}/status", accountNumber)
            .bodyValue(request)
            .retrieve()
            .toEntity(Object.class)
            .map(ResponseEntity::ok);
    }

    // ==================== Transaction Service Routes ====================

    @PostMapping("/transfers")
    public Mono<ResponseEntity<?>> initiateTransfer(@RequestBody Map<String, Object> request) {
        return transactionServiceClient.post()
            .uri("/api/transfers")
            .bodyValue(request)
            .retrieve()
            .toEntity(Object.class)
            .map(ResponseEntity::ok);
    }

    @GetMapping("/transactions/{transactionRef}")
    public Mono<ResponseEntity<?>> getTransaction(@PathVariable String transactionRef) {
        return transactionServiceClient.get()
            .uri("/api/transactions/{ref}", transactionRef)
            .retrieve()
            .toEntity(Object.class)
            .map(ResponseEntity::ok);
    }

    @GetMapping("/accounts/{accountNumber}/transactions")
    public Mono<ResponseEntity<?>> getAccountTransactions(@PathVariable String accountNumber) {
        return transactionServiceClient.get()
            .uri("/api/accounts/{accNo}/transactions", accountNumber)
            .retrieve()
            .toEntity(Object.class)
            .map(ResponseEntity::ok);
    }

    // ==================== Payment Service Routes ====================

    @PostMapping("/payments/generate-qr")
    public Mono<ResponseEntity<?>> generateQR(@RequestBody Map<String, Object> request) {
        return paymentServiceClient.post()
            .uri("/api/payments/generate-qr")
            .bodyValue(request)
            .retrieve()
            .toEntity(Object.class)
            .map(ResponseEntity::ok);
    }

    @PostMapping("/payments/confirm")
    public Mono<ResponseEntity<?>> confirmPayment(@RequestBody Map<String, Object> request) {
        return paymentServiceClient.post()
            .uri("/api/payments/confirm")
            .bodyValue(request)
            .retrieve()
            .toEntity(Object.class)
            .map(ResponseEntity::ok);
    }

    // ==================== Health Check ====================

    @GetMapping("/health")
    public Mono<ResponseEntity<Map<String, String>>> healthCheck() {
        return Mono.just(ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "api-gateway",
            "version", "1.0.0"
        )));
    }
}
