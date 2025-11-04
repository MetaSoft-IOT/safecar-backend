package com.safecar.platform.workshopOps.domain.model.aggregates;

import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.safecar.platform.workshopOps.domain.model.commands.CreateAppointmentCommand;
import com.safecar.platform.workshopOps.domain.model.entities.AppointmentNote;
import com.safecar.platform.workshopOps.domain.model.events.*;
import com.safecar.platform.workshopOps.domain.model.valueobjects.*;
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
     * Workshop Identifier - The identifier of the workshop where the appointment is scheduled.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "workshopId", column = @Column(name = "workshop_id")),
        @AttributeOverride(name = "displayName", column = @Column(name = "workshop_display_name"))
    })
    private WorkshopId workshop;

    /**
     * Vehicle Identifier - The identifier of the vehicle for which the appointment is scheduled.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "vehicleId", column = @Column(name = "vehicle_id")),
        @AttributeOverride(name = "plateNumber", column = @Column(name = "vehicle_plate_number"))
    })
    private VehicleId vehicle;

    /**
     * Driver Identifier - The identifier of the driver associated with the appointment.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "driverId", column = @Column(name = "driver_id")),
        @AttributeOverride(name = "fullName", column = @Column(name = "driver_full_name"))
    })
    private DriverId driver;

    /**
     * Linked Work Order ID - The identifier of the linked work order, if any.
     */
    @Column(name = "linked_work_order_id")
    private Long linkedWorkOrderId;

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
     * Constructs a WorkshopAppointment from a CreateAppointmentCommand.
     * @param command The command containing appointment details
     */
    public WorkshopAppointment(CreateAppointmentCommand command) {
        this();
        this.scheduledAt = command.slot();
        this.workshop = command.workshopId();
        this.vehicle = command.vehicleId();
        this.driver = command.driverId();
        
        registerEvent(new AppointmentCreatedEvent(
            this.getId(), 
            this.workshop, 
            this.vehicle, 
            this.driver, 
            this.scheduledAt, 
            this.linkedWorkOrderId
        ));
    }

    /**
     * Link to Work Order - Links this appointment to a work order using the work order code.
     * @param code The work order code to link to
     */
    public void linkToWorkOrder(WorkOrderCode code) {
        if (code == null) 
            throw new IllegalArgumentException("Work order code cannot be null");

        if (!workshop.workshopId().equals(code.issuedByWorkshopId())) 
            throw new IllegalStateException("Work order must be from the same workshop");

        // TODO: In a real implementation, we would resolve the work order ID from the code
        // For now, we'll use the workshop ID as a placeholder
        this.linkedWorkOrderId = code.issuedByWorkshopId();
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
     * Is Linked To Work Order - This method checks if the appointment is linked to a work order.
     * @return true if linked to a work order, false otherwise
     */
    public boolean isLinkedToWorkOrder() {
        return linkedWorkOrderId != null;
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