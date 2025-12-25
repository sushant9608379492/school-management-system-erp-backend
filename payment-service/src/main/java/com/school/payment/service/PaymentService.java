package com.school.payment.service;

import com.school.payment.dto.CreateOrderRequest;
import com.school.payment.dto.CreateOrderResponse;
import com.school.payment.dto.PaymentStatusResponse;
import com.school.payment.dto.VerifyPaymentRequest;

public interface PaymentService {
    CreateOrderResponse createOrder(CreateOrderRequest request);
    PaymentStatusResponse verifyPayment(VerifyPaymentRequest request);
    boolean verifyWebhookSignature(String payload, String signatureHeader);
}

