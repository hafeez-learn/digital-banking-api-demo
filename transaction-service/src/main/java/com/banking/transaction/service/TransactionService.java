package com.banking.transaction.service;

import com.banking.transaction.dto.*;
import com.banking.transaction.model.Transaction;
import com.banking.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WebClient accountServiceClient;

    public TransactionService(
            TransactionRepository transactionRepository,
            WebClient.Builder accountServiceClientBuilder) {
        this.transactionRepository = transactionRepository;
        String accountServiceUrl = System.getenv("ACCOUNT_SERVICE_URL") != null 
            ? System.getenv("ACCOUNT_SERVICE_URL") 
            : "http://localhost:8081";
        this.accountServiceClient = accountServiceClientBuilder.baseUrl(accountServiceUrl).build();
    }

    @Transactional
    public TransactionResponse initiateTransfer(TransferRequest request) {
        // Validate sufficient balance via Account Service
        boolean hasSufficientBalance = validateBalance(request.getFromAccount(), request.getAmount());
        
        if (!hasSufficientBalance) {
            throw new IllegalStateException("Insufficient balance for transfer");
        }

        // Generate unique transaction reference
        String txnRef = "TXN" + System.currentTimeMillis() + 
            UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        Transaction transaction = new Transaction();
        transaction.setTransactionRef(txnRef);
        transaction.setFromAccount(request.getFromAccount());
        transaction.setToAccount(request.getToAccount());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setType(Transaction.TransactionType.TRANSFER);
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);

        Transaction saved = transactionRepository.save(transaction);
        return TransactionResponse.fromEntity(saved);
    }

    private boolean validateBalance(String accountNumber, BigDecimal amount) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = accountServiceClient.get()
                .uri("/api/accounts/{accNo}/balance", accountNumber)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

            if (response == null) {
                return false;
            }

            Object balanceObj = response.get("balance");
            BigDecimal balance = new BigDecimal(balanceObj.toString());
            return balance.compareTo(amount) >= 0;
        } catch (Exception e) {
            // In case account service is unavailable, log and allow transaction
            System.err.println("Warning: Could not validate balance with account service: " + e.getMessage());
            return true;
        }
    }

    public TransactionResponse getTransaction(String transactionRef) {
        Transaction transaction = transactionRepository.findByTransactionRef(transactionRef)
            .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + transactionRef));
        return TransactionResponse.fromEntity(transaction);
    }

    public List<TransactionResponse> getAccountTransactions(String accountNumber) {
        List<Transaction> transactions = transactionRepository
            .findByFromAccountOrToAccountOrderByCreatedAtDesc(accountNumber, accountNumber);
        return transactions.stream()
            .map(TransactionResponse::fromEntity)
            .toList();
    }
}
