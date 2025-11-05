package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object representing a Vehicle identifier with associated plate number.
 */
@Embeddable
public record VehicleId(
        @Column(name = "vehicle_id", nullable = false)
        Long vehicleId,
        
        @Column(name = "plate_number", nullable = false, length = 20)
        String plateNumber
) {
    public VehicleId {
        if (vehicleId == null || vehicleId <= 0) {
            throw new IllegalArgumentException("Vehicle ID must be a positive value");
        }
        if (plateNumber == null || plateNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Plate number cannot be null or empty");
        }
    }
}