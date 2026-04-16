package com.banking.payment.dto;

import jakarta.validation.constraints.NotBlank;

public class ConfirmPaymentRequest {

    @NotBlank(message = "Payment reference is required")
    private String paymentRef;

    @NotBlank(message = "Payer account is required")
    private String payerAccount;

    // Getters and Setters
    public String getPaymentRef() { return paymentRef; }
    public void setPaymentRef(String paymentRef) { this.paymentRef = paymentRef; }
    public String getPayerAccount() { return payerAccount; }
    public void setPayerAccount(String payerAccount) { this.payerAccount = payerAccount; }
}
