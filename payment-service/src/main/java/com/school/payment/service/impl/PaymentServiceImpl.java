package com.school.payment.service.impl;

import org.springframework.stereotype.Service;
import com.school.payment.client.RazorpayClientWrapper;
import com.school.payment.config.RazorpayConfig;
import com.school.payment.dto.CreateOrderRequest;
import com.school.payment.dto.CreateOrderResponse;
import com.school.payment.dto.PaymentStatusResponse;
import com.school.payment.dto.VerifyPaymentRequest;
import com.school.payment.exception.PaymentException;
import com.school.payment.exception.SignatureVerificationException;
import com.school.payment.service.PaymentService;

import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final RazorpayClientWrapper client;
    private final RazorpayConfig config;

    public PaymentServiceImpl(RazorpayClientWrapper client, RazorpayConfig config) {
        this.client = client;
        this.config = config;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new PaymentException("Invalid amount");
        }
        JSONObject order = (JSONObject) client.createOrder(request.getAmount(), request.getCurrency(), request.getReceipt(), request.getNotes());
        CreateOrderResponse resp = new CreateOrderResponse();
        resp.setRazorpayOrderId(order.optString("id"));
        resp.setAmount(order.optLong("amount"));
        resp.setCurrency(order.optString("currency"));
        resp.setReceipt(order.optString("receipt"));
        resp.setCreatedAt(order.optLong("created_at"));
        return resp;
    }

    @Override
    public PaymentStatusResponse verifyPayment(VerifyPaymentRequest request) {
        boolean ok = verifyPaymentSignature(request.getRazorpayOrderId(), request.getRazorpayPaymentId(), request.getRazorpaySignature());
        if (!ok) {
            throw new SignatureVerificationException("Payment signature verification failed");
        }
        return new PaymentStatusResponse("SUCCESS", "Signature verified");
    }

    private boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
        try {
            if (orderId == null || paymentId == null || signature == null) {
                return false;
            }
            String payload = orderId + "|" + paymentId;
            String secret = config.getKeySecret();
            if (secret == null || secret.isEmpty()) {
                throw new PaymentException("Missing key secret for signature verification");
            }
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            byte[] signatureBytes = decodeSignature(signature);
            if (signatureBytes == null) {
                return false;
            }
            // timing-safe comparison of raw HMAC bytes
            return MessageDigest.isEqual(hash, signatureBytes);
        } catch (Exception e) {
            throw new PaymentException("Failed to verify signature", e);
        }
    }

    @Override
    public boolean verifyWebhookSignature(String payload, String signatureHeader) {
        try {
            if (payload == null || signatureHeader == null) {
                return false;
            }
            String secret = config.getWebhookSecret();
            if (secret == null || secret.isEmpty()) {
                throw new PaymentException("Missing webhook secret for signature verification");
            }
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            byte[] signatureBytes = decodeSignature(signatureHeader);
            if (signatureBytes == null) {
                return false;
            }
            return MessageDigest.isEqual(hash, signatureBytes);
        } catch (Exception e) {
            throw new PaymentException("Failed to verify webhook signature", e);
        }
    }

    // Try decoding signature as hex, then Base64. Return null if both fail.
    private static byte[] decodeSignature(String s) {
        if (s == null) return null;
        s = s.trim();
        // Try hex
        byte[] hex = hexStringToByteArray(s);
        if (hex != null) return hex;
        // Try Base64
        try {
            return Base64.getDecoder().decode(s);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private static byte[] hexStringToByteArray(String s) {
        if (s == null) return null;
        int len = s.length();
        if ((len & 1) != 0) return null; // must be even length
        byte[] data = new byte[len / 2];
        try {
            for (int i = 0; i < len; i += 2) {
                int hi = Character.digit(s.charAt(i), 16);
                int lo = Character.digit(s.charAt(i + 1), 16);
                if (hi == -1 || lo == -1) return null;
                data[i / 2] = (byte) ((hi << 4) + lo);
            }
            return data;
        } catch (Exception ex) {
            return null;
        }
    }
}
