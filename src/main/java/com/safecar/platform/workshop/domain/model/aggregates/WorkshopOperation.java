package com.safecar.platform.workshop.domain.model.aggregates;

import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * Workshop Operation Aggregate
 * <p>
 * Simplified workshop operations focused on core metrics and operational data.
 * Manages workshop performance indicators without physical bay management.
 * </p>
 */
@Getter
@Entity
@Table(name = "workshop_operations")
public class WorkshopOperation extends AuditableAbstractAggregateRoot<WorkshopOperation> {

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "workshopId", column = @Column(name = "workshop_id", nullable = false))
    })
    private WorkshopId workshop;

    @Column(name = "total_appointments", nullable = false)
    private int totalAppointments;

    @Column(name = "total_service_orders", nullable = false)
    private int totalServiceOrders;

    @Column(name = "active_operations", nullable = false)
    private int activeOperations;

    protected WorkshopOperation() {
        this.totalAppointments = 0;
        this.totalServiceOrders = 0;
        this.activeOperations = 0;
    }

    public WorkshopOperation(WorkshopId workshop, int totalAppointments, int totalServiceOrders) {
        this();
        this.workshop = workshop;
        this.totalAppointments = totalAppointments;
        this.totalServiceOrders = totalServiceOrders;
    }

    /**
     * Increments appointment count when a new appointment is created
     */
    public void incrementAppointments() {
        this.totalAppointments++;
    }

    /**
     * Increments service order count and active operations when a new service order is opened
     */
    public void incrementServiceOrders() {
        this.totalServiceOrders++;
        this.activeOperations++;
    }

    /**
     * Decrements active operations when a service order is completed
     */
    public void completeServiceOrder() {
        if (this.activeOperations > 0) {
            this.activeOperations--;
        }
    }

    /**
     * Gets the workshop efficiency ratio (completed vs total service orders)
     * 
     * @return efficiency ratio as percentage (0.0 to 1.0)
     */
    public double getEfficiencyRatio() {
        if (totalServiceOrders == 0) return 0.0;
        int completedOrders = totalServiceOrders - activeOperations;
        return (double) completedOrders / totalServiceOrders;
    }

    /**
     * Getter for total service orders count
     */
    public int getTotalServiceOrders() {
        return totalServiceOrders;
    }
}
