package com.banking.payment.service;

import com.banking.payment.dto.*;
import com.banking.payment.model.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    // Simulated payment repository - in production, use JPA repository
    private Payment.PaymentStatus getPaymentStatusByRef(String ref) {
        // Placeholder - in real implementation, query database
        return Payment.PaymentStatus.PENDING;
    }

    public PaymentResponse generateQr(GenerateQrRequest request) {
        String paymentRef = "PAY" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        
        // Generate PayNow-style QR data
        String qrData = generatePayNowQr(request.getPayeeAccount(), request.getAmount(), paymentRef);

        Payment payment = new Payment();
        payment.setPaymentRef(paymentRef);
        payment.setPayeeAccount(request.getPayeeAccount());
        payment.setAmount(request.getAmount());
        payment.setQrData(qrData);
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        // In real implementation: save to database
        return PaymentResponse.fromEntity(payment);
    }

    @Transactional
    public PaymentResponse confirmPayment(ConfirmPaymentRequest request) {
        // In real implementation: query database for payment
        Payment payment = new Payment();
        payment.setPaymentRef(request.getPaymentRef());
        payment.setPayerAccount(request.getPayerAccount());
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        // Simplified - real implementation would verify and update
        return PaymentResponse.fromEntity(payment);
    }

    public PaymentResponse getPayment(String paymentRef) {
        Payment payment = new Payment();
        payment.setPaymentRef(paymentRef);
        payment.setStatus(Payment.PaymentStatus.PENDING);
        return PaymentResponse.fromEntity(payment);
    }

    private String generatePayNowQr(String payeeAccount, java.math.BigDecimal amount, String ref) {
        // Simplified PayNow QR format simulation
        return String.format("paynow://%s?amt=%.2f&ref=%s", payeeAccount, amount, ref);
    }
}
