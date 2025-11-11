package com.safecar.platform.workshop.application.internal.queryservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.aggregates.WorkshopOperation;
import com.safecar.platform.workshop.domain.model.queries.GetAllWorkshopsQuery;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopOperationByWorkshopIdQuery;
import com.safecar.platform.workshop.domain.services.WorkshopOperationQueryService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.WorkshopOperationRepository;

import java.util.List;
import java.util.Optional;

/**
 * Workshop Operation Query Service Implementation
 * <p>
 * Simplified implementation focused on workshop operations and metrics
 * without service bay management.
 * </p>
 */
@Service
public class WorkshopOperationQueryServiceImpl implements WorkshopOperationQueryService {

    private final WorkshopOperationRepository operationRepository;

    public WorkshopOperationQueryServiceImpl(WorkshopOperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    /**
     * Get a workshop operation by its workshop ID
     */
    @Override
    public Optional<WorkshopOperation> handle(GetWorkshopOperationByWorkshopIdQuery query) {
        return operationRepository.findByWorkshop(query.workshopId());
    }

    /**
     * Get all workshop operations with their metrics
     */
    @Override
    public List<WorkshopOperation> handle(GetAllWorkshopsQuery query) {
        return operationRepository.findAll();
    }
}
