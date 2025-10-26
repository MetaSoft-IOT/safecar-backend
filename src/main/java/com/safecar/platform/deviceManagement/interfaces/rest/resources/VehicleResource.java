package com.safecar.platform.deviceManagement.interfaces.rest.resources;

public record VehicleResource(
        Long id,
        Long driverId,
        String licensePlate,
        String brand,
        String model

) {
}
