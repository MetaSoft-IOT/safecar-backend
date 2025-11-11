package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object representing a Service Order Code with the issuing workshop.
 */
@Embeddable
public record ServiceOrderCode(
        @Column(name = "service_order_code", nullable = false, length = 50) String value,
        @Column(name = "issuing_workshop_id", nullable = false) Long issuedByWorkshopId) {
    public ServiceOrderCode {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException("Service order code cannot be null or empty");

        if (issuedByWorkshopId == null || issuedByWorkshopId <= 0)
            throw new IllegalArgumentException("Issuing workshop ID must be a positive value");
    }
}