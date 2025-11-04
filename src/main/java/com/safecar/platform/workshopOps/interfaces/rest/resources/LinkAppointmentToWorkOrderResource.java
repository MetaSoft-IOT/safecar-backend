package com.safecar.platform.workshopOps.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Link Appointment To Work Order Resource - Represents the data required to
 * link an appointment to a work order.
 * 
 * @param workOrderCode The code of the work order to link the appointment to.
 */
public record LinkAppointmentToWorkOrderResource(
                @NotBlank(message = "Work order code is required") String workOrderCode) {
}