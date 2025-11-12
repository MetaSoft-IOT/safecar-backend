package com.safecar.platform.payments.infrastructure.rest;

import com.safecar.platform.payments.application.dtos.CheckoutSessionResponse;
import com.safecar.platform.payments.application.services.PaymentApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payments", description = "Stripe payment integration and subscription management endpoints")
public class PaymentController {

    private final PaymentApplicationService paymentService;

    public PaymentController(PaymentApplicationService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Debug endpoint to verify payment controller is working.
     */
    @GetMapping("/debug")
    @Operation(summary = "Payment system debug endpoint", 
               description = "Returns system status, available plans, and debug information for testing payment integration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Debug information retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> debugEndpoint() {
        Map<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("status", "Payment controller is working");
        debugInfo.put("timestamp", LocalDateTime.now().toString());
        debugInfo.put("testUserId", "31303200000000000000000000000000");
        debugInfo.put("availablePlans", List.of("BASIC", "PROFESSIONAL", "PREMIUM"));

        // Test response object
        CheckoutSessionResponse testResponse = new CheckoutSessionResponse("debug-session-123");

        Map<String, Object> responseInfo = new HashMap<>();
        responseInfo.put("sessionId", testResponse.getSessionId());
        responseInfo.put("class", testResponse.getClass().getName());
        debugInfo.put("testResponse", responseInfo);

        return ResponseEntity.ok(debugInfo);
    }

    /**
     * Test endpoint for creating checkout sessions without authentication.
     */
    @PostMapping("/test-session")
    @Operation(summary = "Create test checkout session", 
               description = "Creates a test Stripe checkout session with default parameters for integration testing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Test session created successfully"),
            @ApiResponse(responseCode = "400", description = "Error creating test session")
    })
    public ResponseEntity<String> testSession() {
        try {
            // Test directo sin parámetros
            String testSessionId = paymentService.createCheckoutSession(
                    "test-user-123",
                    "BASIC"
            ).getSessionId();

            return ResponseEntity.ok("Session created: " + testSessionId);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Create Stripe checkout session for subscription payment.
     */
    @PostMapping("/checkout-session")
    @Operation(summary = "Create Stripe checkout session", 
               description = "Creates a Stripe checkout session for the specified plan type (BASIC, PROFESSIONAL, PREMIUM)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Checkout session created successfully",
                         content = @Content(schema = @Schema(implementation = CheckoutSessionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid plan type or user ID"),
            @ApiResponse(responseCode = "500", description = "Stripe API error")
    })
    public ResponseEntity<CheckoutSessionResponse> createCheckoutSession(
            @RequestBody CreateCheckoutRequest request,
            @RequestHeader("X-User-Id") String userId) {

        CheckoutSessionResponse response = paymentService.createCheckoutSession(userId, request.getPlanType());
        return ResponseEntity.ok(response);
    }

    /**
     * Request DTO for creating checkout sessions.
     */
    @Schema(description = "Request object for creating a Stripe checkout session")
    public static class CreateCheckoutRequest {
        
        @Schema(description = "Subscription plan type", example = "BASIC", 
                allowableValues = {"BASIC", "PROFESSIONAL", "PREMIUM"})
        private String planType;

        public String getPlanType() {
            return planType;
        }

        public void setPlanType(String planType) {
            this.planType = planType;
        }
    }
}