package com.safecar.platform.workshopOps.domain.model.commands;

import com.safecar.platform.workshopOps.domain.model.valueobjects.*;

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

