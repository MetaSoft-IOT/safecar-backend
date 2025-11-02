package com.safecar.platform.appointments.domain.model.commands;

import java.util.UUID;

public record UpdateAppointmentInformationCommand(
        UUID appointmentId,
        String serviceType,
        String description
) {
}

