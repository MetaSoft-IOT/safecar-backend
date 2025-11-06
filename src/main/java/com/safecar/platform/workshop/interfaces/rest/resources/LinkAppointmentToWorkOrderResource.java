package com.safecar.platform.workshop.interfaces.rest.resources;

/**
 * Link Appointment To Work Order Resource - Represents the raw data required to
 * link an appointment to a work order. Validation will be performed when mapping to domain commands.
 * 
 * @param workOrderCode The code of the work order to link the appointment to.
 */
public record LinkAppointmentToWorkOrderResource(String workOrderCode) {
}