package com.safecar.platform.payments.application.services;

import com.safecar.platform.payments.domain.model.PlanType;
import com.safecar.platform.payments.infrastructure.external.StripePaymentGateway;
import com.safecar.platform.payments.domain.repositories.SubscriptionRepository;
import com.safecar.platform.payments.application.dtos.CheckoutSessionResponse;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentApplicationService {

    private final StripePaymentGateway stripeGateway;
    private final SubscriptionRepository subscriptionRepository;

    public PaymentApplicationService(StripePaymentGateway stripeGateway,
                                     SubscriptionRepository subscriptionRepository) {
        this.stripeGateway = stripeGateway;
        this.subscriptionRepository = subscriptionRepository;
        System.out.println("=== PAYMENT SERVICE INITIALIZED ===");
    }

    public CheckoutSessionResponse createCheckoutSession(String userId, String planType) {
        System.out.println("=== CREATE CHECKOUT SESSION CALLED ===");
        System.out.println("UserId: " + userId);
        System.out.println("PlanType: " + planType);

        try {
            // 1. Validar plan type
            System.out.println("Validating plan type...");
            PlanType plan = PlanType.valueOf(planType.toUpperCase());
            System.out.println("Plan resolved: " + plan);

            // 2. Llamar a Stripe
            System.out.println("Calling Stripe gateway...");
            String sessionId = stripeGateway.createCheckoutSession(userId, plan);
            System.out.println("Stripe session ID: " + sessionId);

            // 3. Crear respuesta
            System.out.println("Creating response...");
            CheckoutSessionResponse response = new CheckoutSessionResponse(sessionId);
            System.out.println("Response sessionId: " + response.getSessionId());

            return response;

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Payment error: " + e.getMessage(), e);
        }
    }

    public void handleSubscriptionCreated(String userId, String stripeSubscriptionId, String planType) {
        PlanType plan = PlanType.valueOf(planType.toUpperCase());
        com.safecar.platform.payments.domain.model.Subscription subscription =
                new com.safecar.platform.payments.domain.model.Subscription(userId, plan, stripeSubscriptionId);
        subscriptionRepository.save(subscription);
    }
}