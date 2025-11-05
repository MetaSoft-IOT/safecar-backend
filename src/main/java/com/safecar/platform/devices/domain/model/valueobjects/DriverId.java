package com.safecar.platform.devices.domain.model.valueobjects;

/**
 * Driver ID - Value Object representing a Driver's unique identifier.
 * 
 * @param driverId the unique identifier of the driver
 */
public record DriverId(Long driverId) {
    public DriverId {
        if (driverId <= 0) {
            throw new IllegalArgumentException("Driver ID must be a positive non-null value.");
        }
    }

    public DriverId() {
        this(0L);
    }
}
