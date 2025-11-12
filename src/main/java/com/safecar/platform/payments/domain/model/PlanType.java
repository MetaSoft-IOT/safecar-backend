package com.safecar.platform.payments.domain.model;

public enum PlanType {
    BASIC("price_1SQbsT3l890Fc29CerlSwh4r"),      // ← Usa los IDs REALES de Stripe
    PROFESSIONAL("price_1SQbt23l890Fc29CqoqLYCnu"),
    PREMIUM("price_1SQbtK3l890Fc29COSEZ6iK4");

    private final String stripePriceId;

    PlanType(String stripePriceId) {
        this.stripePriceId = stripePriceId;
    }

    public String getStripePriceId() {
        return stripePriceId;
    }
}