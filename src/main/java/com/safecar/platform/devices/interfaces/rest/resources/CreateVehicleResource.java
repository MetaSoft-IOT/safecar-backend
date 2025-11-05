package com.safecar.platform.devices.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Create Vehicle Resource
 * <p>
 * This record represents the data required to create a new vehicle associated
 * with a driver. It includes validation constraints to ensure data integrity.
 * </p>
 * 
 * @param driverId     the ID of the driver to whom the vehicle will be associated
 * @param licensePlate the license plate of the vehicle
 * @param brand        the brand of the vehicle
 * @param model        the model of the vehicle
 */
public record CreateVehicleResource(
        @NotNull(message = "Driver ID is required")
        @Positive(message = "Driver ID must be positive")
        Long driverId,
        
        @NotBlank(message = "License plate is required")
        @Size(min = 3, max = 15, message = "License plate must be between 3 and 15 characters")
        String licensePlate,
        
        @NotBlank(message = "Brand is required")
        @Size(min = 2, max = 50, message = "Brand must be between 2 and 50 characters")
        String brand,
        
        @NotBlank(message = "Model is required")
        @Size(min = 1, max = 50, message = "Model must be between 1 and 50 characters")
        String model
) {
}
