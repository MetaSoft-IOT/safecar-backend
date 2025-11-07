package com.safecar.platform.payments.infrastructure.persistence;

import com.safecar.platform.payments.domain.model.Subscription;
import com.safecar.platform.payments.domain.repositories.SubscriptionRepository;
import org.springframework.stereotype.Repository;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final Map<String, Subscription> subscriptions = new ConcurrentHashMap<>();
    private final Map<String, String> userToSubscription = new ConcurrentHashMap<>();
    private final Map<String, String> stripeToSubscription = new ConcurrentHashMap<>();

    @Override
    public Subscription save(Subscription subscription) {
        subscriptions.put(subscription.getId(), subscription);
        userToSubscription.put(subscription.getUserId(), subscription.getId());
        stripeToSubscription.put(subscription.getStripeSubscriptionId(), subscription.getId());
        return subscription;
    }

    @Override
    public Subscription findByUserId(String userId) {
        String subscriptionId = userToSubscription.get(userId);
        return subscriptionId != null ? subscriptions.get(subscriptionId) : null;
    }

    @Override
    public Subscription findByStripeSubscriptionId(String stripeSubscriptionId) {
        String subscriptionId = stripeToSubscription.get(stripeSubscriptionId);
        return subscriptionId != null ? subscriptions.get(subscriptionId) : null;
    }
}