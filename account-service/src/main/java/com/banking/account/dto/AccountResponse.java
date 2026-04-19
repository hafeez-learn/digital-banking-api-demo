package com.banking.account.dto;

import com.banking.account.model.Account;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountResponse {

    private String accountNumber;
    private String holderName;
    private String nricMasked;  // NRIC is NEVER exposed - only masked for display
    private BigDecimal balance;
    private String status;
    private LocalDateTime createdAt;

    public static AccountResponse fromEntity(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountNumber(account.getAccountNumber());
        response.setHolderName(account.getHolderName());
        // NRIC is masked - only show last 4 chars if present
        response.setNricMasked(maskNric(account.getNric()));
        response.setBalance(account.getBalance());
        response.setStatus(account.getStatus().name());
        response.setCreatedAt(account.getCreatedAt());
        return response;
    }

    /**
     * Masks NRIC for display purposes.
     * Example: S1234567A -> ****567A
     * Returns null if NRIC is null or empty.
     */
    private static String maskNric(String nric) {
        if (nric == null || nric.isEmpty()) {
            return null;
        }
        if (nric.length() <= 4) {
            return "****";
        }
        return "****" + nric.substring(nric.length() - 4);
    }

    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }
    public String getNricMasked() { return nricMasked; }
    public void setNricMasked(String nricMasked) { this.nricMasked = nricMasked; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
