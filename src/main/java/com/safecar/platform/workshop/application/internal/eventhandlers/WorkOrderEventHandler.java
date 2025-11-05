package com.safecar.platform.workshop.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.events.WorkOrderAppointmentAddedEvent;
import com.safecar.platform.workshop.domain.model.events.WorkOrderClosedEvent;
import com.safecar.platform.workshop.domain.model.events.WorkOrderOpenedEvent;

import java.sql.Timestamp;

/**
 * Event handler for Work Order related events.
 * <p>
 *     These events are triggered when work orders are opened, closed, or appointments are added.
 *     Used for tracking work order lifecycle and integration with external systems.
 * </p>
 */
@Service
public class WorkOrderEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkOrderEventHandler.class);

    /**
     * Event listener for WorkOrderOpenedEvent.
     * <p>
     *     This method is triggered when a new work order is opened.
     * </p>
     *
     * @param event the {@link WorkOrderOpenedEvent} event.
     */
    @EventListener
    public void on(WorkOrderOpenedEvent event) {
        LOGGER.info("Work Order opened with ID {} for workshop {} and vehicle {} at {}", 
                   event.workOrderId(), event.workshopId(), event.vehicleId(), currentTimestamp());
        
        // Business logic: Initialize work order tracking and resource allocation
        LOGGER.info("WORK ORDER LIFECYCLE - Code: {}, Driver: {}, Opened: {}", 
                   event.code(), event.driverId(), event.openedAt());
        
        // TODO: Integrate with Inventory BC for parts/tools reservation
        // TODO: Integrate with Mechanics BC for technician assignment
        // TODO: Integrate with Billing BC to create billing record
        // TODO: Integrate with Tracking BC for work progress monitoring
    }

    /**
     * Event listener for WorkOrderClosedEvent.
     * <p>
     *     This method is triggered when a work order is closed.
     * </p>
     *
     * @param event the {@link WorkOrderClosedEvent} event.
     */
    @EventListener
    public void on(WorkOrderClosedEvent event) {
        LOGGER.info("Work Order with ID {} closed at {}", 
                   event.workOrderId(), event.closedAt());
        
        // Business logic: Finalize work order and trigger completion workflows
        LOGGER.info("WORK ORDER COMPLETED - All appointments finished, resources being released for work order ID: {}", 
                   event.workOrderId());
        
        // TODO: Integrate with Billing BC to finalize invoice and payment processing
        // TODO: Integrate with Inventory BC to release reserved parts/tools
        // TODO: Integrate with Quality BC for service quality assessment
        // TODO: Integrate with Analytics BC for completion metrics
    }

    /**
     * Event listener for WorkOrderAppointmentAddedEvent.
     * <p>
     *     This method is triggered when an appointment is added to a work order.
     * </p>
     *
     * @param event the {@link WorkOrderAppointmentAddedEvent} event.
     */
    @EventListener
    public void on(WorkOrderAppointmentAddedEvent event) {
        LOGGER.info("Appointment added to Work Order ID {}. Service slot: {}. Total appointments: {} at {}", 
                   event.workOrderId(), event.slot(), event.totalAppointments(), currentTimestamp());
        
        // Business logic: Track appointment additions and update work order state
        LOGGER.info("WORK ORDER GROWTH - Service: {} - {}, Progress: {}/{} appointments", 
                   event.slot().startAt(), event.slot().endAt(), 
                   event.totalAppointments(), event.totalAppointments()); // Could track planned vs actual
        
        // TODO: Integrate with Scheduling BC for resource planning
        // TODO: Integrate with Progress BC for work order timeline updates  
        // TODO: Update estimated completion time based on appointment count
    }

    /**
     * Get the current timestamp.
     *
     * @return the current timestamp.
     */
    private Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}