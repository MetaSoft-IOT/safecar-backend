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
        @AttributeOverrides({
            @AttributeOverride(name = "vehicleId", column = @Column(name = "sample_vehicle_id")),
            @AttributeOverride(name = "plateNumber", column = @Column(name = "sample_plate_number"))
        })
        VehicleId vehicleId,
        
        @Embedded
        @AttributeOverrides({
            @AttributeOverride(name = "driverId", column = @Column(name = "sample_driver_id")),
            @AttributeOverride(name = "fullName", column = @Column(name = "sample_driver_name"))
        })
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
        FaultCode dtc,

        @Embedded
        @AttributeOverrides({
            @AttributeOverride(name = "frontLeft", column = @Column(name = "sample_tire_fl_psi", precision = 5, scale = 2)),
            @AttributeOverride(name = "frontRight", column = @Column(name = "sample_tire_fr_psi", precision = 5, scale = 2)),
            @AttributeOverride(name = "rearLeft", column = @Column(name = "sample_tire_rl_psi", precision = 5, scale = 2)),
            @AttributeOverride(name = "rearRight", column = @Column(name = "sample_tire_rr_psi", precision = 5, scale = 2))
        })
        TirePressure tirePressure,

        @Embedded
        @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "sample_cabin_gas_type", length = 40)),
            @AttributeOverride(name = "concentrationPpm", column = @Column(name = "sample_cabin_gas_ppm", precision = 10, scale = 2))
        })
        CabinGasLevel cabinGasLevel,

        @Embedded
        @AttributeOverrides({
            @AttributeOverride(name = "lateralG", column = @Column(name = "sample_accel_lat_g", precision = 6, scale = 3)),
            @AttributeOverride(name = "longitudinalG", column = @Column(name = "sample_accel_long_g", precision = 6, scale = 3)),
            @AttributeOverride(name = "verticalG", column = @Column(name = "sample_accel_vert_g", precision = 6, scale = 3))
        })
        AccelerationVector accelerationVector
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
