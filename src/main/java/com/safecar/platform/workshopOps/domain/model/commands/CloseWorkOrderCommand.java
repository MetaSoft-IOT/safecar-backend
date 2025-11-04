package com.safecar.platform.workshopOps.domain.model.commands;

/**
 * Close Work Order - Command to close an existing work order.
 *
 * @param workOrderId the id of the work order to be closed
 */
public record CloseWorkOrderCommand(
        Long workOrderId) {
}
