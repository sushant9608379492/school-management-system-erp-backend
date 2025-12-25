package com.school.payment.controller;

import com.school.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class WebhookController {

    private final PaymentService service;

    public WebhookController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestBody String payload, @RequestHeader(value = "X-Razorpay-Signature", required = false) String signature) {
        boolean ok = service.verifyWebhookSignature(payload, signature);
        if (!ok) {
            return ResponseEntity.status(401).body("Invalid signature");
        }
        // TODO: process event, idempotency, etc.
        return ResponseEntity.ok().body("ok");
    }
}

