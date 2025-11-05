package com.safecar.platform.workshopOps.domain.model.commands;

import com.safecar.platform.workshopOps.domain.model.valueobjects.ServiceSlot;

/**
 * Add Appointment To Work Order - Command to add an appointment to an existing
 * work order.
 * 
 * @param workOrderId the id of the work order
 * @param slot        the service slot to be added
 */
public record AddAppointmentToWorkOrderCommand(
        Long workOrderId,
        ServiceSlot slot) {
}
