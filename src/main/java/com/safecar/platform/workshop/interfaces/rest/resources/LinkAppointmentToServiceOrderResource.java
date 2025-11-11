package com.safecar.platform.workshop.interfaces.rest.resources;

/**
 * Link Appointment To Service Order Resource - Represents the raw data required to
 * link an appointment to a service order. Validation will be performed when mapping to domain commands.
 * 
 * @param serviceOrderCode The code of the service order to link the appointment to.
 */
public record LinkAppointmentToServiceOrderResource(String serviceOrderCode) {
}