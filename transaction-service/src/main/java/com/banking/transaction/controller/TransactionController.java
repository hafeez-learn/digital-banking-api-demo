package com.banking.transaction.controller;

import com.banking.transaction.dto.*;
import com.banking.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfers")
    public ResponseEntity<TransactionResponse> initiateTransfer(@Valid @RequestBody TransferRequest request) {
        TransactionResponse response = transactionService.initiateTransfer(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions/{transactionRef}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable String transactionRef) {
        TransactionResponse response = transactionService.getTransaction(transactionRef);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/accounts/{accountNumber}/transactions")
    public ResponseEntity<List<TransactionResponse>> getAccountTransactions(@PathVariable String accountNumber) {
        List<TransactionResponse> transactions = transactionService.getAccountTransactions(accountNumber);
        return ResponseEntity.ok(transactions);
    }
}
