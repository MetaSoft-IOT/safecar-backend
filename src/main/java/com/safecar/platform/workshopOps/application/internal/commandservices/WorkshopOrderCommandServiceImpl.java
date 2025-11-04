package com.safecar.platform.workshopOps.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshopOps.domain.model.aggregates.WorkshopOrder;
import com.safecar.platform.workshopOps.domain.model.commands.*;
import com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories.WorkshopOrderRepository;
import com.safecar.platform.workshopOps.domain.services.WorkshopOrderCommandService;

/**
 * Workshop Order Command Service Implementation
 * 
 * @see WorkshopOrderCommandService WorkshopOrderCommandService for method
 *      details.
 */
@Service
public class WorkshopOrderCommandServiceImpl implements WorkshopOrderCommandService {

    /**
     * Repository for WorkshopOrder to handle persistence operations.
     */
    private final WorkshopOrderRepository repository;

    /**
     * Constructor for WorkshopOrderCommandServiceImpl.
     * 
     * @param repository Repository for WorkshopOrder to handle persistence
     *                   operations.
     */
    public WorkshopOrderCommandServiceImpl(WorkshopOrderRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(OpenWorkOrderCommand command) {
        var exists = repository.existsByCodeValueAndWorkshop_WorkshopId(command.code().value(),
                command.workshopId().workshopId());
                
        if (exists)
            throw new IllegalStateException("Work order code already exists for this workshop");

        var wo = new WorkshopOrder(command);
        repository.save(wo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(CloseWorkOrderCommand command) {
        var wo = repository.findById(command.workOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Work order not found"));
        wo.close();
        repository.save(wo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(AddAppointmentToWorkOrderCommand command) {
        var wo = repository.findById(command.workOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Work order not found"));
        wo.addAppointment(command.slot());
        repository.save(wo);
    }
}
