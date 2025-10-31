package com.safecar.platform.deviceManagement.interfaces.rest.resources;

public record CreateVehicleResource(
        Long driverId,
        String licensePlate,
        String brand,
        String model
) {
}
