package com.banking.payment.service;

import com.banking.payment.dto.*;
import com.banking.payment.model.Payment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void generateQr_Success() {
        GenerateQrRequest request = new GenerateQrRequest();
        request.setPayeeAccount("ACC987654321");
        request.setAmount(BigDecimal.valueOf(50.00));
        request.setDescription("Test payment");

        PaymentResponse response = paymentService.generateQr(request);

        assertNotNull(response);
        assertNotNull(response.getPaymentRef());
        assertTrue(response.getPaymentRef().startsWith("PAY"));
        assertEquals("ACC987654321", response.getPayeeAccount());
        assertEquals(BigDecimal.valueOf(50.00), response.getAmount());
        assertEquals("PENDING", response.getStatus());
        assertNotNull(response.getQrData());
        assertTrue(response.getQrData().contains("paynow://"));
        assertNotNull(response.getExpiryTime());
    }

    @Test
    void generateQr_PaymentRefFormat() {
        GenerateQrRequest request = new GenerateQrRequest();
        request.setPayeeAccount("ACC123456789");
        request.setAmount(BigDecimal.valueOf(100.00));

        PaymentResponse response = paymentService.generateQr(request);

        // Payment ref should be PAY + timestamp + random suffix
        assertNotNull(response.getPaymentRef());
        assertTrue(response.getPaymentRef().length() > 10);
        assertTrue(response.getPaymentRef().startsWith("PAY"));
    }

    @Test
    void confirmPayment_Success() {
        ConfirmPaymentRequest request = new ConfirmPaymentRequest();
        request.setPaymentRef("PAY1234567890ABCD");
        request.setPayerAccount("ACC123456789");

        PaymentResponse response = paymentService.confirmPayment(request);

        assertNotNull(response);
        assertEquals("PAY1234567890ABCD", response.getPaymentRef());
        assertEquals("ACC123456789", response.getPayerAccount());
        assertEquals("COMPLETED", response.getStatus());
    }

    @Test
    void getPayment_Success() {
        PaymentResponse response = paymentService.getPayment("PAY1234567890ABCD");

        assertNotNull(response);
        assertEquals("PAY1234567890ABCD", response.getPaymentRef());
        assertEquals("PENDING", response.getStatus());
    }

    @Test
    void generateQr_ExpiryTime_IsSet() {
        GenerateQrRequest request = new GenerateQrRequest();
        request.setPayeeAccount("ACC987654321");
        request.setAmount(BigDecimal.valueOf(25.00));

        PaymentResponse response = paymentService.generateQr(request);

        assertNotNull(response.getExpiryTime());
        assertTrue(response.getExpiryTime().isAfter(LocalDateTime.now()));
    }

    @Test
    void generateQr_QrDataFormat() {
        GenerateQrRequest request = new GenerateQrRequest();
        request.setPayeeAccount("ACC555555555");
        request.setAmount(BigDecimal.valueOf(99.99));

        PaymentResponse response = paymentService.generateQr(request);

        String qrData = response.getQrData();
        assertTrue(qrData.contains("ACC555555555"));
        assertTrue(qrData.contains("99.99"));
        assertTrue(qrData.contains(response.getPaymentRef()));
    }

    @Test
    void confirmPayment_StatusIsCompleted() {
        ConfirmPaymentRequest request = new ConfirmPaymentRequest();
        request.setPaymentRef("PAY9999999999");
        request.setPayerAccount("ACC111111111");

        PaymentResponse response = paymentService.confirmPayment(request);

        assertEquals("COMPLETED", response.getStatus());
    }

    @Test
    void generateQr_AmountMatches() {
        BigDecimal testAmount = BigDecimal.valueOf(123.45);
        GenerateQrRequest request = new GenerateQrRequest();
        request.setPayeeAccount("ACC123456789");
        request.setAmount(testAmount);

        PaymentResponse response = paymentService.generateQr(request);

        assertEquals(0, testAmount.compareTo(response.getAmount()));
    }
}
