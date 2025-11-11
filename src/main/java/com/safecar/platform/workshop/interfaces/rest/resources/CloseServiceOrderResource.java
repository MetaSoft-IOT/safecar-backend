package com.safecar.platform.workshop.interfaces.rest.resources;

/**
 * Close Service Order Resource - Resource for closing a service order.
 * <p>
 * In SafeCar's operational context, closing a service order triggers:
 * - Final telemetry data processing and analysis
 * - Predictive maintenance recommendations generation
 * - Workshop performance metrics update
 * </p>
 * 
 * @param reason optional reason for closing the service order
 * @param notes optional closing notes from workshop staff
 */
public record CloseServiceOrderResource(
        Long serviceOrderId,
        String reason,
        String notes) {
}