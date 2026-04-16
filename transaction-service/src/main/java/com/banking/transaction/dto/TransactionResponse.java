package com.banking.transaction.dto;

import com.banking.transaction.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private String transactionRef;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String type;
    private String status;
    private LocalDateTime createdAt;

    public static TransactionResponse fromEntity(Transaction txn) {
        TransactionResponse response = new TransactionResponse();
        response.setTransactionRef(txn.getTransactionRef());
        response.setFromAccount(txn.getFromAccount());
        response.setToAccount(txn.getToAccount());
        response.setAmount(txn.getAmount());
        response.setType(txn.getType().name());
        response.setStatus(txn.getStatus().name());
        response.setCreatedAt(txn.getCreatedAt());
        return response;
    }

    // Getters and Setters
    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }
    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }
    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
