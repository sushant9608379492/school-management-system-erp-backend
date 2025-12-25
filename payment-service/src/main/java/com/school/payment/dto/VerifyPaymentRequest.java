package com.school.payment.dto;

import jakarta.validation.constraints.NotBlank;

public class VerifyPaymentRequest {
    private String razorpaySignature;
    @NotBlank

    private String razorpayOrderId;
    @NotBlank

    private String razorpayPaymentId;
    @NotBlank

    public void setRazorpaySignature(String razorpaySignature) {
        this.razorpaySignature = razorpaySignature;
    }

    public String getRazorpaySignature() {
        return razorpaySignature;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

}




