package com.safecar.platform.workshop.domain.model.commands;

import com.safecar.platform.workshop.domain.model.valueobjects.*;

/**
 * Command to create a new workshop appointment.
 */
public record CreateAppointmentCommand(
        WorkshopId workshopId,
        VehicleId vehicleId,
        DriverId driverId,
        ServiceSlot slot
) {
}

