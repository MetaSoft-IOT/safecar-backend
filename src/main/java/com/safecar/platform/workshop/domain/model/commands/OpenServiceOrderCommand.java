package com.safecar.platform.workshop.domain.model.commands;

import java.time.Instant;

import com.safecar.platform.workshop.domain.model.valueobjects.*;

/**
 * Open Service Order - Command to open a new service order.
 * 
 * @param workshopId the id of the workshop
 * @param vehicleId  the id of the vehicle
 * @param driverId   the id of the driver
 * @param code       the service order code
 * @param openedAt   the timestamp when the service order was opened
 */
public record OpenServiceOrderCommand(
        WorkshopId workshopId,
        VehicleId vehicleId,
        DriverId driverId,
        ServiceOrderCode code,
        Instant openedAt) {
}
