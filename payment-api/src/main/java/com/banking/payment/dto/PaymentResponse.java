package com.banking.payment.dto;

import com.banking.payment.model.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {

    private String paymentRef;
    private String payerAccount;
    private String payeeAccount;
    private BigDecimal amount;
    private String qrData;
    private String status;
    private LocalDateTime expiryTime;
    private LocalDateTime createdAt;

    public static PaymentResponse fromEntity(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentRef(payment.getPaymentRef());
        response.setPayerAccount(payment.getPayerAccount());
        response.setPayeeAccount(payment.getPayeeAccount());
        response.setAmount(payment.getAmount());
        response.setQrData(payment.getQrData());
        response.setStatus(payment.getStatus().name());
        response.setExpiryTime(payment.getExpiryTime());
        response.setCreatedAt(payment.getCreatedAt());
        return response;
    }

    // Getters and Setters
    public String getPaymentRef() { return paymentRef; }
    public void setPaymentRef(String paymentRef) { this.paymentRef = paymentRef; }
    public String getPayerAccount() { return payerAccount; }
    public void setPayerAccount(String payerAccount) { this.payerAccount = payerAccount; }
    public String getPayeeAccount() { return payeeAccount; }
    public void setPayeeAccount(String payeeAccount) { this.payeeAccount = payeeAccount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getQrData() { return qrData; }
    public void setQrData(String qrData) { this.qrData = qrData; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getExpiryTime() { return expiryTime; }
    public void setExpiryTime(LocalDateTime expiryTime) { this.expiryTime = expiryTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
