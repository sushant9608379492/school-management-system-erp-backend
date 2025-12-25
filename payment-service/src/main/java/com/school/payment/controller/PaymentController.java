package com.school.payment.controller;

import com.school.payment.dto.CreateOrderRequest;
import com.school.payment.dto.CreateOrderResponse;
import com.school.payment.dto.PaymentStatusResponse;
import com.school.payment.dto.VerifyPaymentRequest;
import com.school.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/orders")
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        CreateOrderResponse resp = service.createOrder(request);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentStatusResponse> verifyPayment(@Valid @RequestBody VerifyPaymentRequest request) {
        PaymentStatusResponse resp = service.verifyPayment(request);
        return ResponseEntity.ok(resp);
    }
}

