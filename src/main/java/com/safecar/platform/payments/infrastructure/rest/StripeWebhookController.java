package com.safecar.platform.payments.infrastructure.rest;

import com.safecar.platform.payments.application.services.PaymentApplicationService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StripeWebhookController {

    private final PaymentApplicationService paymentService;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    public StripeWebhookController(PaymentApplicationService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/webhooks/stripe")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            if ("customer.subscription.created".equals(event.getType())) {
                Subscription subscription = (Subscription) event.getDataObjectDeserializer().getObject().get();

                // Extraer metadata
                String userId = subscription.getMetadata().get("user_id");
                String planType = "BASIC"; // Puedes ajustar esto según tu lógica

                paymentService.handleSubscriptionCreated(userId, subscription.getId(), planType);
            }

            return ResponseEntity.ok().build();

        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing webhook");
        }
    }
}