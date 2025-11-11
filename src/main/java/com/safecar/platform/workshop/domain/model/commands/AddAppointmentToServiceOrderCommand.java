package com.safecar.platform.workshop.domain.model.commands;

import com.safecar.platform.workshop.domain.model.valueobjects.ServiceSlot;

/**
 * Add Appointment To Service Order - Command to add an appointment to an existing
 * service order.
 * 
 * @param serviceOrderId the id of the service order
 * @param slot           the service slot to be added
 */
public record AddAppointmentToServiceOrderCommand(
        Long serviceOrderId,
        ServiceSlot slot) {
}
