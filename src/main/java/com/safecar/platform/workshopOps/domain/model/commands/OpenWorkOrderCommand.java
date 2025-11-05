package com.safecar.platform.workshopOps.domain.model.commands;

import com.safecar.platform.workshopOps.domain.model.valueobjects.*;
import java.time.Instant;

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
