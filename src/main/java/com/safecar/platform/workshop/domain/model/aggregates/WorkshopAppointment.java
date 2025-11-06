package com.safecar.platform.workshop.domain.model.aggregates;

import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.safecar.platform.workshop.domain.model.commands.CreateAppointmentCommand;
import com.safecar.platform.workshop.domain.model.entities.AppointmentNote;
import com.safecar.platform.workshop.domain.model.events.*;
import com.safecar.platform.workshop.domain.model.valueobjects.*;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Workshop Appointment Aggregate - This aggregate Root represents an appointment in the workshop operations context.
 */
@Getter
@Entity
@Table(name = "workshop_appointments")
public class WorkshopAppointment extends AuditableAbstractAggregateRoot<WorkshopAppointment> {

    /**
     * Appointment Status - This enum represents the various states an appointment can be in.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    /**
     * Scheduled Service Slot - The time slot for which the appointment is scheduled.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "startAt", column = @Column(name = "scheduled_start_at")),
        @AttributeOverride(name = "endAt", column = @Column(name = "scheduled_end_at"))
    })
    private ServiceSlot scheduledAt;

    /**
     * Work Order Reference - Reference to the work order that contains all entity IDs.
     * This eliminates redundancy of workshop, vehicle, and driver IDs by using the work order as single source.
     * The work order contains the complete context of the service (workshop + vehicle + driver).
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "work_order_id")
    private WorkshopOrder workOrder;

    /**
     * List of notes - The notes associated with the appointment.
     */
    @OneToMany(mappedBy = "workshopAppointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<AppointmentNote> notes;

    /**
     * Default constructor for JPA.
     */
    protected WorkshopAppointment() {
        this.status = AppointmentStatus.PENDING;
        this.notes = new ArrayList<>();
    }

    /**
     * Constructs a WorkshopAppointment from a CreateAppointmentCommand and a WorkshopOrder.
     * @param command The command containing appointment details
     * @param workOrder The work order that contains all related entity IDs
     */
    public WorkshopAppointment(CreateAppointmentCommand command, WorkshopOrder workOrder) {
        this();
        this.scheduledAt = command.slot();
        this.workOrder = workOrder;
        
        registerEvent(new AppointmentCreatedEvent(
            this.getId(), 
            workOrder.getWorkshop(), 
            workOrder.getVehicle(), 
            workOrder.getDriver(), 
            this.scheduledAt, 
            workOrder.getId()
        ));
    }

    /**
     * Link to Work Order - Links this appointment to a work order using the work order code.
     * @param code The work order code to link to
     */
    public void linkToWorkOrder(WorkOrderCode code) {
        if (code == null) 
            throw new IllegalArgumentException("Work order code cannot be null");
        // This method kept for backward compatibility; it requires repository resolution
        throw new UnsupportedOperationException("linkToWorkOrder(code) is not supported; use service to resolve work order id and call updateWorkOrder(workOrderId, code)");
    }

    /**
     * Update Work Order - Changes the work order associated with this appointment.
     * @param newWorkOrder the new work order to link to
     * @param code work order code used for validation  
     */
    public void updateWorkOrder(WorkshopOrder newWorkOrder, WorkOrderCode code) {
        if (code == null)
            throw new IllegalArgumentException("Work order code cannot be null");
        if (newWorkOrder == null)
            throw new IllegalArgumentException("Work order cannot be null");
        
        // Validation that workshops match
        if (!this.workOrder.getWorkshop().workshopId().equals(newWorkOrder.getWorkshop().workshopId()))
            throw new IllegalStateException("New work order must be from the same workshop");
        
        this.workOrder = newWorkOrder;
    }

    /**
     * Reschedules - This method reschedules the appointment to a new time slot.
     * @param slot The new service slot
     */
    public void reschedule(ServiceSlot slot) {
        if (slot == null) 
            throw new IllegalArgumentException("Service slot cannot be null");

        if (AppointmentStatus.COMPLETED.equals(this.status) || AppointmentStatus.CANCELLED.equals(this.status)) 
            throw new IllegalStateException("Cannot reschedule completed or cancelled appointments");
        
        var oldSlot = this.scheduledAt;
        this.scheduledAt = slot;
        
        registerEvent(new AppointmentRescheduledEvent(this.getId(), oldSlot, slot));
    }

    /**
     * Cancels - This method cancels the appointment.
     */
    public void cancel() {
        if (AppointmentStatus.COMPLETED.equals(this.status)) 
            throw new IllegalStateException("Cannot cancel completed appointments");
        this.status = AppointmentStatus.CANCELLED;
        
        registerEvent(new AppointmentCanceledEvent(this.getId(), Instant.now()));
    }

    /**
     * Confirm - This method confirm the appointment.
     */
    public void confirm() {
        if (!AppointmentStatus.PENDING.equals(this.status)) 
            throw new IllegalStateException("Only pending appointments can be confirmed");
        this.status = AppointmentStatus.CONFIRMED;
    }

    /**
     * Start - This method starts the appointment service.
     */
    public void start() {
        if (!AppointmentStatus.CONFIRMED.equals(this.status)) 
            throw new IllegalStateException("Only confirmed appointments can be started");
        this.status = AppointmentStatus.IN_PROGRESS;
    }

    /**
     * Complete - This method complete the appointment.
     */
    public void complete() {
        if (!AppointmentStatus.IN_PROGRESS.equals(this.status)) 
            throw new IllegalStateException("Only in-progress appointments can be completed");
        this.status = AppointmentStatus.COMPLETED;
    }

    /**
     * Get Work Order ID - Gets the ID of the associated work order.
     * @return the work order ID 
     */
    public Long getWorkOrderId() {
        return workOrder != null ? workOrder.getId() : null;
    }

    /**
     * Get Work Order - Gets the associated work order.
     * @return the work order
     */
    public WorkshopOrder getWorkOrder() {
        return workOrder;
    }

    /**
     * Is Linked To Work Order - This method checks if the appointment is linked to a work order.
     * @return true if linked to a work order, false otherwise
     */
    public boolean isLinkedToWorkOrder() {
        return workOrder != null;
    }

    // Convenience methods to access IDs from the associated WorkOrder (avoiding redundancy)
    
    /**
     * Get Workshop ID - Gets workshop ID from the associated work order
     * @return WorkshopId from work order, null if no work order linked
     */
    public WorkshopId getWorkshop() {
        return workOrder != null ? workOrder.getWorkshop() : null;
    }

    /**
     * Get Vehicle ID - Gets vehicle ID from the associated work order
     * @return VehicleId from work order, null if no work order linked
     */
    public VehicleId getVehicle() {
        return workOrder != null ? workOrder.getVehicle() : null;
    }

    /**
     * Get Driver ID - Gets driver ID from the associated work order
     * @return DriverId from work order, null if no work order linked
     */
    public DriverId getDriver() {
        return workOrder != null ? workOrder.getDriver() : null;
    }

    /**
     * Add Note - This method add notes to the current appointment.
     * @param content The content of the note
     * @param authorId The ID of the author
     */
    public void addNote(String content, Long authorId) {
        if (content == null || content.trim().isEmpty()) 
            throw new IllegalArgumentException("Note content cannot be null or empty");
        if (authorId == null) 
            throw new IllegalArgumentException("Author ID cannot be null");
        
        var note = new AppointmentNote(content, authorId, this);
        this.notes.add(note);
    }


}