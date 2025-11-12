package com.safecar.platform.payments.infrastructure.rest;

import com.safecar.platform.payments.application.services.PaymentApplicationService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Stripe Webhooks", description = "Stripe webhook endpoints for handling payment events")
public class StripeWebhookController {

    private final PaymentApplicationService paymentService;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    public StripeWebhookController(PaymentApplicationService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Handle Stripe webhook events for subscription lifecycle.
     * This endpoint receives events from Stripe when subscriptions are created, updated, or deleted.
     */
    @PostMapping("/webhooks/stripe")
    @Operation(summary = "Handle Stripe webhook events", 
               description = "Receives and processes webhook events from Stripe for subscription management (customer.subscription.created, etc.)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Webhook event processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid signature or malformed event")
    })
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