package com.safecar.platform.appointments.domain.model.commands;

import java.util.UUID;

public record StartAppointmentCommand(UUID appointmentId) {
}

