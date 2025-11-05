package com.safecar.platform.workshopOps.domain.model.events;

import com.safecar.platform.workshopOps.domain.model.valueobjects.*;

/**
 * Event fired when an appointment is created.
 */
public record AppointmentCreatedEvent(
        Long appointmentId,
        WorkshopId workshopId,
        VehicleId vehicleId,
        DriverId driverId,
        ServiceSlot slot,
        Long workOrderId // Optional, can be null
) {
}