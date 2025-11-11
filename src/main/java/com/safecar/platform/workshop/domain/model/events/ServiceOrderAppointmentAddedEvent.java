package com.safecar.platform.workshop.domain.model.events;

import com.safecar.platform.workshop.domain.model.valueobjects.ServiceSlot;

/**
 * Service Order Appointment Added Event - Event fired when an appointment is added
 * to a service order.
 * 
 * @param serviceOrderId    the id of the service order
 * @param slot              the service slot that was added
 * @param totalAppointments the total number of appointments after addition
 */
public record ServiceOrderAppointmentAddedEvent(
        Long serviceOrderId,
        ServiceSlot slot,
        int totalAppointments) {
}
