package com.safecar.platform.workshopOps.domain.model.events;

import com.safecar.platform.workshopOps.domain.model.valueobjects.*;
import java.time.Instant;

/**
 * Work Order Opened Event - Event fired when a work order is opened.
 * 
 * @param workOrderId the id of the work order
 * @param workshopId  the id of the workshop
 * @param vehicleId   the id of the vehicle
 * @param driverId    the id of the driver
 * @param code        the work order code
 * @param openedAt    the timestamp when the work order was opened
 */
public record WorkOrderOpenedEvent(
        Long workOrderId,
        WorkshopId workshopId,
        VehicleId vehicleId,
        DriverId driverId,
        WorkOrderCode code,
        Instant openedAt) {
}
