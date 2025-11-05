package com.safecar.platform.workshop.domain.model.commands;

/**
 * Close Work Order - Command to close an existing work order.
 *
 * @param workOrderId the id of the work order to be closed
 */
public record CloseWorkOrderCommand(
        Long workOrderId) {
}
