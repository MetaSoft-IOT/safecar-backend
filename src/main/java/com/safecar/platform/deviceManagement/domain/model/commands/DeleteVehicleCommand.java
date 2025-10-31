package com.safecar.platform.deviceManagement.domain.model.commands;

public record DeleteVehicleCommand(
        Long vehicleId,
        Long driverId){
    public DeleteVehicleCommand {
        if (vehicleId == null || vehicleId <= 0) {
            throw new IllegalArgumentException("Vehicle ID must be a positive non-null value.");
        }
    }
}
