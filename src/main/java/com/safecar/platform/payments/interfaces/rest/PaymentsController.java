package com.safecar.platform.payments.interfaces.rest;

import com.safecar.platform.payments.domain.services.PaymentCommandService;
import com.safecar.platform.payments.interfaces.rest.resources.CheckoutSessionResource;
import com.safecar.platform.payments.interfaces.rest.resources.CreateCheckoutSessionResource;
import com.safecar.platform.payments.interfaces.rest.transform.CheckoutSessionResourceFromSessionIdAssembler;
import com.safecar.platform.payments.interfaces.rest.transform.CreateCheckoutSessionCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
public class PaymentsController {

    private final PaymentCommandService paymentCommandService;

    public PaymentsController(PaymentCommandService paymentCommandService) {
        this.paymentCommandService = paymentCommandService;
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

        Map<String, Object> responseInfo = new HashMap<>();
        responseInfo.put("sessionId", "debug-session-123");
        responseInfo.put("class", "CheckoutSessionResource");
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
            var testResource = new CreateCheckoutSessionResource("BASIC");
            var command = CreateCheckoutSessionCommandFromResourceAssembler
                    .toCommandFromResource("test-user-123", testResource);
            String sessionId = paymentCommandService.handle(command);

            return ResponseEntity.ok("Session created: " + sessionId);

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
                         content = @Content(schema = @Schema(implementation = CheckoutSessionResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid plan type or user ID"),
            @ApiResponse(responseCode = "500", description = "Stripe API error")
    })
    public ResponseEntity<CheckoutSessionResource> createCheckoutSession(
            @Valid @RequestBody CreateCheckoutSessionResource resource,
            @RequestHeader("X-User-Id") String userId) {

        var command = CreateCheckoutSessionCommandFromResourceAssembler
                .toCommandFromResource(userId, resource);
        String sessionId = paymentCommandService.handle(command);
        var response = CheckoutSessionResourceFromSessionIdAssembler
                .toResourceFromSessionId(sessionId);
        
        return ResponseEntity.ok(response);
    }
}