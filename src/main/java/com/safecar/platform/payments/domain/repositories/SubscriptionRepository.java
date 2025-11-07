package com.safecar.platform.payments.domain.repositories;

import com.safecar.platform.payments.domain.model.Subscription;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);
    Subscription findByUserId(String userId);
    Subscription findByStripeSubscriptionId(String stripeSubscriptionId);
}