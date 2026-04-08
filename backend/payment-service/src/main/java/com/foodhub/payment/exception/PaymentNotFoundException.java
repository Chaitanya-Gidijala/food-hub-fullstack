package com.foodhub.payment.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String sessionId) {
        super("Payment not found with session id: " + sessionId);
    }
}
