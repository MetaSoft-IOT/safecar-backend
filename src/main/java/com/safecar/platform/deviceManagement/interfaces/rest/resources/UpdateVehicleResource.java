package com.safecar.platform.deviceManagement.interfaces.rest.resources;

public record UpdateVehicleResource(
        Long driverId,
        String licensePlate,
        String brand,
        String model
) {
}
