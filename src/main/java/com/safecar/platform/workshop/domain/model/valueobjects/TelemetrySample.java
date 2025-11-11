package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.*;

/**
 * Value Object representing a telemetry sample with all possible telemetry data.
 */
@Embeddable
public record TelemetrySample(
        @Enumerated(EnumType.STRING)
        @Column(name = "telemetry_type", nullable = false)
        TelemetryType type,
        
        @Enumerated(EnumType.STRING)
        @Column(name = "alert_severity", nullable = false)
        AlertSeverity severity,
        
        @Embedded
        @AttributeOverride(name = "occurredAt", column = @Column(name = "sample_occurred_at"))
        TelemetryTimestamp timestamp,
        
        @Embedded
        @AttributeOverride(name = "vehicleId", column = @Column(name = "sample_vehicle_id"))
        VehicleId vehicleId,
        
        @Embedded
        @AttributeOverride(name = "driverId", column = @Column(name = "sample_driver_id"))
        DriverId driverId,
        
        @Embedded
        @AttributeOverride(name = "value", column = @Column(name = "sample_speed"))
        SpeedKmh speed,
        
        @Embedded
        @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "sample_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "sample_longitude"))
        })
        GeoPoint location,
        
        @Embedded
        @AttributeOverride(name = "value", column = @Column(name = "sample_odometer"))
        OdometerKm odometer,
        
        @Embedded
        @AttributeOverrides({
            @AttributeOverride(name = "code", column = @Column(name = "sample_fault_code")),
            @AttributeOverride(name = "standard", column = @Column(name = "sample_fault_standard"))
        })
        FaultCode dtc
) {
    public TelemetrySample {
        if (type == null) {
            throw new IllegalArgumentException("Telemetry type cannot be null");
        }
        if (severity == null) {
            throw new IllegalArgumentException("Alert severity cannot be null");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        if (vehicleId == null) {
            throw new IllegalArgumentException("Vehicle ID cannot be null");
        }
        if (driverId == null) {
            throw new IllegalArgumentException("Driver ID cannot be null");
        }
    }
}