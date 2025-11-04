package com.safecar.platform.workshopOps.domain.model.aggregates;

import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.safecar.platform.workshopOps.domain.model.entities.ServiceBay;
import com.safecar.platform.workshopOps.domain.model.events.ServiceBayAllocatedEvent;
import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkshopId;
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
        // no-op for now
        //TODO: implement mechanic assignment logic
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
