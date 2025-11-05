package com.safecar.platform.workshopOps.domain.model.commands;

import com.safecar.platform.workshopOps.domain.model.valueobjects.ServiceSlot;

/**
 * Command to reschedule an appointment.
 */
public record RescheduleAppointmentCommand(
        Long appointmentId,
        ServiceSlot slot
) {
}

