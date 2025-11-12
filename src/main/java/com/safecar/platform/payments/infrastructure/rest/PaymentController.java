package com.safecar.platform.payments.infrastructure.rest;

import com.safecar.platform.payments.application.dtos.CheckoutSessionResponse;
import com.safecar.platform.payments.application.services.PaymentApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentApplicationService paymentService;

    public PaymentController(PaymentApplicationService paymentService) {
        this.paymentService = paymentService;
    }

    // Endpoint de debug
    @GetMapping("/debug")
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

    // Endpoint simplificado para testing
    @PostMapping("/test-session")
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

    // Endpoint principal
    @PostMapping("/checkout-session")
    public ResponseEntity<CheckoutSessionResponse> createCheckoutSession(
            @RequestBody CreateCheckoutRequest request,
            @RequestHeader("X-User-Id") String userId) {

        CheckoutSessionResponse response = paymentService.createCheckoutSession(userId, request.getPlanType());
        return ResponseEntity.ok(response);
    }

    // Clase interna para el request
    public static class CreateCheckoutRequest {
        private String planType;

        public String getPlanType() {
            return planType;
        }

        public void setPlanType(String planType) {
            this.planType = planType;
        }
    }
}