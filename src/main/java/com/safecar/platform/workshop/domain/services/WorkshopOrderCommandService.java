package com.safecar.platform.workshop.domain.services;

import com.safecar.platform.workshop.domain.model.commands.AddAppointmentToWorkOrderCommand;
import com.safecar.platform.workshop.domain.model.commands.CloseWorkOrderCommand;
import com.safecar.platform.workshop.domain.model.commands.OpenWorkOrderCommand;

/**
 * Workshop Order Command Service - handles commands related to workshop orders.
 */
public interface WorkshopOrderCommandService {
    /**
     * Handles opening a new work order.
     * @param command the command to open a work order
     */
    void handle(OpenWorkOrderCommand command);
    /**
     * Handles closing an existing work order.
     * @param command the command to close a work order
     */
    void handle(CloseWorkOrderCommand command);
    /**
     * Handles adding an appointment to an existing work order.
     * @param command the command to add an appointment
     */
    void handle(AddAppointmentToWorkOrderCommand command);
}
