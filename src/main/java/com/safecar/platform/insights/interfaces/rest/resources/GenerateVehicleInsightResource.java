package com.safecar.platform.insights.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

public record GenerateVehicleInsightResource(
        Long driverId,
        String driverFullName,
        Long vehicleId,
        String plateNumber,
        Instant capturedAt,
        String severity,
        BigDecimal speedKmh,
        LocationResource location,
        TirePressureResource tirePressure,
        CabinGasResource cabinGas,
        AccelerationResource acceleration
) {
    public record LocationResource(BigDecimal latitude, BigDecimal longitude) {
    }

    public record TirePressureResource(
            BigDecimal frontLeft,
            BigDecimal frontRight,
            BigDecimal rearLeft,
            BigDecimal rearRight
    ) {
    }

    public record CabinGasResource(String type, BigDecimal ppm) {
    }

    public record AccelerationResource(
            BigDecimal lateralG,
            BigDecimal longitudinalG,
            BigDecimal verticalG
    ) {
    }
}
