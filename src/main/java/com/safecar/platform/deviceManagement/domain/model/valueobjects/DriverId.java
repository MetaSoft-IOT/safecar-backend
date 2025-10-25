package com.safecar.platform.deviceManagement.domain.model.valueobjects;

public record DriverId(Long driverId) {
    public DriverId {
        if ( driverId <= 0) {
            throw new IllegalArgumentException("Driver ID must be a positive non-null value.");
        }
    }
    public DriverId() {
        this(0L);
    }
}
