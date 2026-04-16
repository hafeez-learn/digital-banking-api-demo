package com.banking.account.dto;

import jakarta.validation.constraints.*;

public class AccountRequest {

    @NotBlank(message = "Account number is required")
    @Size(min = 8, max = 20, message = "Account number must be 8-20 characters")
    private String accountNumber;

    @NotBlank(message = "Holder name is required")
    @Size(max = 100, message = "Holder name too long")
    private String holderName;

    @Pattern(regexp = "^[A-Z0-9]{9}$", message = "NRIC must be 9 alphanumeric characters")
    private String nric;

    private Double initialBalance;

    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }
    public String getNric() { return nric; }
    public void setNric(String nric) { this.nric = nric; }
    public Double getInitialBalance() { return initialBalance; }
    public void setInitialBalance(Double initialBalance) { this.initialBalance = initialBalance; }
}
