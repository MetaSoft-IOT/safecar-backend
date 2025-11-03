package com.safecar.platform.workshopOps.domain.model.commands;

import java.util.UUID;

public record UpdateAppointmentInformationCommand(
        UUID appointmentId,
        String serviceType,
        String description
) {
}

