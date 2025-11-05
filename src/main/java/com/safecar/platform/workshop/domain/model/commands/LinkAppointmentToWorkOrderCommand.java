package com.safecar.platform.workshop.domain.model.commands;

import com.safecar.platform.workshop.domain.model.valueobjects.WorkOrderCode;

/**
 * Command to link an appointment to a work order.
 */
public record LinkAppointmentToWorkOrderCommand(
        Long appointmentId,
        WorkOrderCode workOrderCode
) {
}