package com.safecar.platform.workshop.application.internal.queryservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.aggregates.WorkshopOrder;
import com.safecar.platform.workshop.domain.model.queries.*;
import com.safecar.platform.workshop.domain.services.WorkshopOrderQueryService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.WorkshopOrderRepository;

import java.util.List;
import java.util.Optional;

/**
 * Workshop Order Query Service Implementation
 * 
 * @see WorkshopOrderQueryService WorkshopOrderQueryService for method details.
 */
@Service
public class WorkshopOrderQueryServiceImpl implements WorkshopOrderQueryService {

    /**
     * Repository for WorkshopOrder to handle persistence operations.
     */
    private final WorkshopOrderRepository repository;

    /**
     * Constructor for WorkshopOrderQueryServiceImpl.
     * 
     * @param repository Repository for WorkshopOrder to handle persistence
     *                   operations.
     */
    public WorkshopOrderQueryServiceImpl(WorkshopOrderRepository repository) {
        this.repository = repository;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<WorkshopOrder> handle(GetWorkOrderByIdQuery query) {
        return repository.findById(query.workOrderId());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorkshopOrder> handle(GetWorkOrdersByWorkshopQuery query) {
        if (query.status() == null) 
            return repository.findByWorkshop(query.workshopId());
        return repository.findByWorkshopAndStatus(query.workshopId(), query.status());
    }
}
