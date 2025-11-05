package com.safecar.platform.workshop.domain.model.aggregates;

import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.safecar.platform.workshop.domain.model.commands.OpenWorkOrderCommand;
import com.safecar.platform.workshop.domain.model.events.*;
import com.safecar.platform.workshop.domain.model.valueobjects.*;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.Instant;

/**
 * Workshop Order Aggregate - represents a work order containing appointments.
 */
@Getter
@Entity
@Table(name = "workshop_orders")
public class WorkshopOrder extends AuditableAbstractAggregateRoot<WorkshopOrder> {

    /**
     * Work Order Status - The current status of the work order.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WorkOrderStatus status;

    /**
     * Work Order Code - Unique code identifying the work order.
     */
    @Embedded
    private WorkOrderCode code;

    /**
     * Workshop Identifier - The identifier of the workshop where the work order is
     * created.
     */
    @Embedded
    private WorkshopId workshop;

    /**
     * Vehicle Identifier - The identifier of the vehicle associated with the work
     * order.
     */
    @Embedded
    private VehicleId vehicle;

    /**
     * Driver Identifier - The identifier of the driver associated with the work
     * order.
     */
    @Embedded
    private DriverId driver;

    /**
     * Opened At - The timestamp when the work order was opened.
     */
    @Column(name = "opened_at", nullable = false)
    private Instant openedAt;

    /**
     * Closed At - The timestamp when the work order was closed.
     */
    @Column(name = "closed_at")
    private Instant closedAt;

    /**
     * Total Appointments - The total number of appointments linked to this work
     * order.
     */
    @Column(name = "total_appointments", nullable = false)
    private int totalAppointments;

    /**
     * Default constructor for JPA.
     */
    protected WorkshopOrder() {
        this.status = WorkOrderStatus.OPEN;
        this.totalAppointments = 0;
    }

    /**
     * Constructs a WorkshopOrder from an OpenWorkOrderCommand.
     * 
     * @param command The command containing order details
     */
    public WorkshopOrder(OpenWorkOrderCommand command) {
        this();
        this.workshop = command.workshopId();
        this.vehicle = command.vehicleId();
        this.driver = command.driverId();
        this.code = command.code();
        this.openedAt = command.openedAt() == null ? Instant.now() : command.openedAt();

        registerEvent(new WorkOrderOpenedEvent(
                this.getId(),
                this.workshop,
                this.vehicle,
                this.driver,
                this.code,
                this.openedAt));
    }

    /**
     * Add Appointment - Increments the total appointments counter.
     * 
     * @param slot The service slot being added
     */
    public void addAppointment(ServiceSlot slot) {
        if (slot == null)
            throw new IllegalArgumentException("Service slot cannot be null");
        if (WorkOrderStatus.CLOSED.equals(this.status))
            throw new IllegalStateException("Cannot add appointment to closed work order");
        this.totalAppointments = this.totalAppointments + 1;
        // Register an event signalling that an appointment was added to this work order
        registerEvent(new WorkOrderAppointmentAddedEvent(this.getId(), slot, this.totalAppointments));
    }

    /**
     * Remove Appointment - Decrements the total appointments counter.
     * 
     * @param slot The service slot being removed
     */
    public void removeAppointment(ServiceSlot slot) {
        if (slot == null)
            throw new IllegalArgumentException("Service slot cannot be null");
        if (this.totalAppointments <= 0)
            throw new IllegalStateException("No appointments to remove");
        this.totalAppointments = this.totalAppointments - 1;
    }

    /**
     * Is Closable - Check if the work order can be closed.
     * 
     * @return true if the work order is closable, false otherwise
     */
    public boolean isClosable() {
        return totalAppointments == 0 && WorkOrderStatus.OPEN.equals(this.status);
    }

    /**
     * Close - Closes the work order.
     */
    public void close() {
        if (!isClosable())
            throw new IllegalStateException("Work order is not closable");
        this.status = WorkOrderStatus.CLOSED;
        this.closedAt = Instant.now();
        registerEvent(new WorkOrderClosedEvent(this.getId(), this.closedAt));
    }
}
