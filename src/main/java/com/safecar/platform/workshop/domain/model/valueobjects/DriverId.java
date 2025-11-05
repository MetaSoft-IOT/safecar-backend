package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object representing a Driver identifier with associated full name.
 */
@Embeddable
public record DriverId(
        @Column(name = "driver_id", nullable = false)
        Long driverId,
        
        @Column(name = "driver_full_name", nullable = false, length = 200)
        String fullName
) {
    public DriverId {
        if (driverId == null || driverId <= 0) {
            throw new IllegalArgumentException("Driver ID must be a positive value");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
    }
}