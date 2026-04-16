package com.banking.account.dto;

import com.banking.account.model.Account;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountResponse {

    private String accountNumber;
    private String holderName;
    private String nric;
    private BigDecimal balance;
    private String status;
    private LocalDateTime createdAt;

    public static AccountResponse fromEntity(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountNumber(account.getAccountNumber());
        response.setHolderName(account.getHolderName());
        response.setNric(account.getNric());
        response.setBalance(account.getBalance());
        response.setStatus(account.getStatus().name());
        response.setCreatedAt(account.getCreatedAt());
        return response;
    }

    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }
    public String getNric() { return nric; }
    public void setNric(String nric) { this.nric = nric; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
