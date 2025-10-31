package com.safecar.platform.deviceManagement.domain.model.commands;

public record CreateVehicleCommand(
        Long driverId,
        String licensePlate,
        String brand,
        String model) {


    public CreateVehicleCommand {
        if (driverId == null || driverId <= 0) {
            throw new IllegalArgumentException("Driver ID must be a positive non-null value.");
        }
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("License plate must be a non-null, non-blank value.");
        }
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand must be a non-null, non-blank value.");
        }
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model must be a non-null, non-blank value.");
        }
    }
}
