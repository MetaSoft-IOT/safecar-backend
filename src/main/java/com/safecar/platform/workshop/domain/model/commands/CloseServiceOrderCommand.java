package com.safecar.platform.workshop.domain.model.commands;

/**
 * Close Service Order - Command to close an existing service order.
 *
 * @param serviceOrderId the id of the service order to be closed
 */
public record CloseServiceOrderCommand(
        Long serviceOrderId,
        String reason,
        String notes
        ) {
}
