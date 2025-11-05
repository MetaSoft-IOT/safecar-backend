package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object representing a Work Order Code with the issuing workshop.
 */
@Embeddable
public record WorkOrderCode(
        @Column(name = "work_order_code", nullable = false, length = 50)
        String value,
        
        @Column(name = "issuing_workshop_id", nullable = false)
        Long issuedByWorkshopId
) {
    public WorkOrderCode {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Work order code cannot be null or empty");
        }
        if (issuedByWorkshopId == null || issuedByWorkshopId <= 0) {
            throw new IllegalArgumentException("Issuing workshop ID must be a positive value");
        }
    }
}