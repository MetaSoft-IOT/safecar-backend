package com.safecar.platform.workshopOps.domain.model.events;

import com.safecar.platform.workshopOps.domain.model.valueobjects.ServiceSlot;

/**
 * Work Order Appointment Added Event - Event fired when an appointment is added
 * to a work order.
 * 
 * @param workOrderId       the id of the work order
 * @param slot              the service slot that was added
 * @param totalAppointments the total number of appointments after addition
 */
public record WorkOrderAppointmentAddedEvent(
        Long workOrderId,
        ServiceSlot slot,
        int totalAppointments) {
}
