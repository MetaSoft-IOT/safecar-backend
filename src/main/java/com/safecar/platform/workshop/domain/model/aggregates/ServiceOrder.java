package com.safecar.platform.workshop.domain.model.aggregates;

import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.safecar.platform.workshop.domain.model.commands.OpenServiceOrderCommand;
import com.safecar.platform.workshop.domain.model.events.*;
import com.safecar.platform.workshop.domain.model.valueobjects.*;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.Instant;
import java.util.Arrays;

/**
 * Service Order Aggregate - represents a service order containing appointments.
 */
@Getter
@Entity
@Table(name = "service_orders")
public class ServiceOrder extends AuditableAbstractAggregateRoot<ServiceOrder> {

    /**
     * Service Order Status - The current status of the service order.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ServiceOrderStatus status;

    /**
     * Service Order Code - Unique code identifying the service order.
     */
    @Embedded
    private ServiceOrderCode code;

    /**
     * Workshop Identifier - The identifier of the workshop where the service order
     * is
     * created.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "workshopId", column = @Column(name = "workshop_id", nullable = false))
    })
    private WorkshopId workshop;

    /**
     * Vehicle Identifier - The identifier of the vehicle associated with the
     * service
     * order.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "vehicleId", column = @Column(name = "vehicle_id", nullable = false))
    })
    private VehicleId vehicle;

    /**
     * Driver Identifier - The identifier of the driver associated with the service
     * order.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "driverId", column = @Column(name = "driver_id", nullable = false))
    })
    private DriverId driver;

    /**
     * Opened At - The timestamp when the service order was opened.
     */
    @Column(name = "opened_at", nullable = false)
    private Instant openedAt;

    /**
     * Closed At - The timestamp when the service order was closed.
     */
    @Column(name = "closed_at")
    private Instant closedAt;

    /**
     * Total Appointments - The total number of appointments linked to this service
     * order.
     */
    @Column(name = "total_appointments", nullable = false)
    private int totalAppointments;

    /**
     * Default constructor for JPA.
     */
    protected ServiceOrder() {
        this.status = ServiceOrderStatus.OPEN;
        this.totalAppointments = 0;
    }

    /**
     * Constructs a ServiceOrder from an OpenServiceOrderCommand.
     * 
     * @param command The command containing order details
     */
    public ServiceOrder(OpenServiceOrderCommand command) {
        this();
        this.workshop = command.workshopId();
        this.vehicle = command.vehicleId();
        this.driver = command.driverId();
        this.code = command.code();
        this.openedAt = command.openedAt() == null ? Instant.now() : command.openedAt();

        registerEvent(new ServiceOrderOpenedEvent(
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
        if (ServiceOrderStatus.CLOSED.equals(this.status))
            throw new IllegalStateException("Cannot add appointment to closed service order");
        this.totalAppointments = this.totalAppointments + 1;
        // Register an event signalling that an appointment was added to this service
        // order
        registerEvent(new ServiceOrderAppointmentAddedEvent(this.getId(), slot, this.totalAppointments));
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
     * Close - Closes the service order if possible.
     * 
     * @return true if the service order was successfully closed, false if it cannot
     *         be closed
     */
    public boolean close() {
        // Validate if the service order can be closed
        if (totalAppointments > 0 || ServiceOrderStatus.CLOSED.equals(this.status)) {
            return false;
        }

        this.status = ServiceOrderStatus.CLOSED;
        this.closedAt = Instant.now();
        registerEvent(new ServiceOrderClosedEvent(this.getId(), this.closedAt));
        return true;
    }

    /**
     * Get Workshop - Returns the workshop identifier.
     * 
     * @return the workshop identifier
     */
    public WorkshopId getWorkshop() {
        return this.workshop;
    }

    /**
     * Get Vehicle - Returns the vehicle identifier.
     * 
     * @return the vehicle identifier
     */
    public VehicleId getVehicle() {
        return this.vehicle;
    }

    /**
     * Get Driver - Returns the driver identifier.
     * 
     * @return the driver identifier
     */
    public DriverId getDriver() {
        return this.driver;
    }

    /**
     * Get Code - Returns the service order code.
     * 
     * @return the service order code
     */
    public ServiceOrderCode getCode() {
        return this.code;
    }

    /**
     * Get Status - Returns the service order status.
     * 
     * @return the service order status
     */
    public ServiceOrderStatus getStatus() {
        return this.status;
    }

    /**
     * Get Opened At - Returns the opened timestamp.
     * 
     * @return the opened timestamp
     */
    public Instant getOpenedAt() {
        return this.openedAt;
    }

    /**
     * Get Closed At - Returns the closed timestamp.
     * 
     * @return the closed timestamp
     */
    public Instant getClosedAt() {
        return this.closedAt;
    }

    /**
     * Check if Service Order Status exists
     * @param status the service order status to check
     * @return true if the status exists, false otherwise
     */
    public boolean existsServiceOrderStatus(ServiceOrderStatus status) {
        return Arrays
                .stream(ServiceOrderStatus.values())
                .anyMatch(s -> s == status);
    }
}
