package com.banking.payment.controller;

import com.banking.payment.dto.*;
import com.banking.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/generate-qr")
    public ResponseEntity<PaymentResponse> generateQr(@Valid @RequestBody GenerateQrRequest request) {
        PaymentResponse response = paymentService.generateQr(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm")
    public ResponseEntity<PaymentResponse> confirmPayment(@Valid @RequestBody ConfirmPaymentRequest request) {
        PaymentResponse response = paymentService.confirmPayment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentRef}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String paymentRef) {
        PaymentResponse response = paymentService.getPayment(paymentRef);
        return ResponseEntity.ok(response);
    }
}
