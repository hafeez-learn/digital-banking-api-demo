package com.banking.transaction.service;

import com.banking.transaction.dto.*;
import com.banking.transaction.model.Transaction;
import com.banking.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionResponse initiateTransfer(TransferRequest request) {
        // Generate unique transaction reference
        String txnRef = "TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        Transaction transaction = new Transaction();
        transaction.setTransactionRef(txnRef);
        transaction.setFromAccount(request.getFromAccount());
        transaction.setToAccount(request.getToAccount());
        transaction.setAmount(request.getAmount());
        transaction.setType(Transaction.TransactionType.TRANSFER);
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED); // Simplified - real impl would call Account Service

        Transaction saved = transactionRepository.save(transaction);
        return TransactionResponse.fromEntity(saved);
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
            .collect(Collectors.toList());
    }
}
