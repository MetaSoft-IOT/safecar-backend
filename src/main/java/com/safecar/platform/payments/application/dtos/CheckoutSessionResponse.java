package com.safecar.platform.payments.application.dtos;

public class CheckoutSessionResponse {
    private final String sessionId;

    public CheckoutSessionResponse(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }
}