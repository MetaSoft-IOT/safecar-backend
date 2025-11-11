package com.safecar.platform.workshop.domain.model.events;

import java.time.Instant;

import com.safecar.platform.workshop.domain.model.valueobjects.*;

/**
 * Service Order Opened Event - Event fired when a service order is opened.
 * 
 * @param serviceOrderId the id of the service order
 * @param workshopId     the id of the workshop
 * @param vehicleId      the id of the vehicle
 * @param driverId       the id of the driver
 * @param code           the service order code
 * @param openedAt       the timestamp when the service order was opened
 */
public record ServiceOrderOpenedEvent(
        Long serviceOrderId,
        WorkshopId workshopId,
        VehicleId vehicleId,
        DriverId driverId,
        ServiceOrderCode code,
        Instant openedAt) {
}
