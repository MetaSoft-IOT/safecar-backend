package com.safecar.platform.workshop.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Create Service Order Resource - Resource for creating a new service order.
 * <p>
 * In SafeCar context, service orders represent the core operational unit that links:
 * - Workshop operations with vehicle IoT data processing
 * - Driver-vehicle relationships for predictive maintenance
 * - Telemetry analysis with workshop service delivery
 * </p>
 * 
 * @param workshopId the ID of the workshop creating the service order
 * @param vehicleId the ID of the vehicle requiring service
 * @param driverId the ID of the driver associated with the vehicle
 * @param serviceDescription optional description of services to be performed
 */
public record CreateServiceOrderResource(
        @NotNull @Positive Long workshopId,
        @NotNull @Positive Long vehicleId,
        @NotNull @Positive Long driverId,
        String serviceDescription) {
}