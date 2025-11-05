package com.safecar.platform.workshop.domain.model.commands;

import java.time.Instant;

import com.safecar.platform.workshop.domain.model.valueobjects.*;

/**
 * Open Work Order - Command to open a new work order.
 * 
 * @param workshopId the id of the workshop
 * @param vehicleId  the id of the vehicle
 * @param driverId   the id of the driver
 * @param code       the work order code
 * @param openedAt   the timestamp when the work order was opened
 */
public record OpenWorkOrderCommand(
        WorkshopId workshopId,
        VehicleId vehicleId,
        DriverId driverId,
        WorkOrderCode code,
        Instant openedAt) {
}
