package com.safecar.platform.payments.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Subscription {
    // Getters
    private String id;
    private String userId;  // ← String para UUID
    private PlanType plan;
    private String status;
    private String stripeSubscriptionId;
    private LocalDateTime createdAt;

    public Subscription(String userId, PlanType plan, String stripeSubscriptionId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.plan = plan;
        this.status = "ACTIVE";
        this.stripeSubscriptionId = stripeSubscriptionId;
        this.createdAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = "CANCELLED";
    }
}