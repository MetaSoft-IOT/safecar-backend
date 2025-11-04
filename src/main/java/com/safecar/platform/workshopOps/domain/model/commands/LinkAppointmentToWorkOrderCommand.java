package com.safecar.platform.workshopOps.domain.model.commands;

import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkOrderCode;

/**
 * Command to link an appointment to a work order.
 */
public record LinkAppointmentToWorkOrderCommand(
        Long appointmentId,
        WorkOrderCode workOrderCode
) {
}