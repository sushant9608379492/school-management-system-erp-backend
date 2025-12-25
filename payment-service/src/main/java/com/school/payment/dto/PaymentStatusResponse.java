package com.school.payment.dto;

public class PaymentStatusResponse {
    private String message;
    private String status;

    public PaymentStatusResponse() {}
    public PaymentStatusResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}




