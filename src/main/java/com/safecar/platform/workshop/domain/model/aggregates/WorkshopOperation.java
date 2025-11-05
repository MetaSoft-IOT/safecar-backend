package com.safecar.platform.workshop.domain.model.aggregates;

import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.safecar.platform.workshop.domain.model.entities.ServiceBay;
import com.safecar.platform.workshop.domain.model.events.ServiceBayAllocatedEvent;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "workshop_operations")
public class WorkshopOperation extends AuditableAbstractAggregateRoot<WorkshopOperation> {

    @Embedded
    private WorkshopId workshop;

    @Column(name = "mechanics_count", nullable = false)
    private int mechanicsCount;

    @Column(name = "bays_count", nullable = false)
    private int baysCount;

    @OneToMany(mappedBy = "workshopOperation", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ServiceBay> bays = new ArrayList<>();

    protected WorkshopOperation() {
    }

    public WorkshopOperation(WorkshopId workshop, int mechanicsCount, int baysCount) {
        this.workshop = workshop;
        this.mechanicsCount = mechanicsCount;
        this.baysCount = baysCount;
    }

    public void assignMechanic(Long userId) {
        // basic placeholder: we might track assignments in future
        if (userId == null) throw new IllegalArgumentException("userId cannot be null");
        // TODO: Implement mechanic assignment logic when Mechanics BC is available
        // This would require: 1) Validation of mechanic existence, 2) Capacity tracking, 3) Schedule management
        throw new UnsupportedOperationException("Mechanic assignment requires Mechanics bounded context integration");
    }

    public ServiceBay allocateBay(String label) {
        if (label == null || label.trim().isEmpty()) throw new IllegalArgumentException("label required");
        var bay = new ServiceBay(label, this.workshop, this);
        this.bays.add(bay);
        this.baysCount = this.bays.size();
        registerEvent(new ServiceBayAllocatedEvent(bay.getId(), this.workshop, label));
        return bay;
    }
}
