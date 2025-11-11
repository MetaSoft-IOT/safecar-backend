package com.safecar.platform.workshop.domain.services;

import com.safecar.platform.workshop.domain.model.commands.AddAppointmentToServiceOrderCommand;
import com.safecar.platform.workshop.domain.model.commands.CloseServiceOrderCommand;
import com.safecar.platform.workshop.domain.model.commands.OpenServiceOrderCommand;

import java.util.Optional;

import com.safecar.platform.workshop.domain.model.aggregates.ServiceOrder;

/**
 * Service Order Command Service - handles commands related to service orders.
 */
public interface ServiceOrderCommandService {
    /**
     * Handles opening a new service order.
     * 
     * @param command the command to open a service order
     */
    Optional<ServiceOrder> handle(OpenServiceOrderCommand command);

    /**
     * Handles closing an existing service order.
     * 
     * @param command the command to close a service order
     */
    Optional<ServiceOrder> handle(CloseServiceOrderCommand command);

    /**
     * Handles adding an appointment to an existing service order.
     * 
     * @param command the command to add an appointment
     */
    Optional<ServiceOrder> handle(AddAppointmentToServiceOrderCommand command);
}
