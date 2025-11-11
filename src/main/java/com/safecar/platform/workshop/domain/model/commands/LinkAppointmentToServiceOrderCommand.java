package com.safecar.platform.workshop.domain.model.commands;

import com.safecar.platform.workshop.domain.model.valueobjects.ServiceOrderCode;

/**
 * Command to link an appointment to a service order.
 */
public record LinkAppointmentToServiceOrderCommand(
        Long appointmentId,
        ServiceOrderCode serviceOrderCode
) {
}