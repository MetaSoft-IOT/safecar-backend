package com.safecar.platform.workshop.domain.services;

import java.util.List;
import java.util.Optional;

import com.safecar.platform.workshop.domain.model.aggregates.WorkshopOperation;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopOperationByWorkshopIdQuery;
import com.safecar.platform.workshop.domain.model.queries.GetAllWorkshopsQuery;

/**
 * Workshop Operation Query Service
 * <p>
 * Simplified query service focusing on workshop operations and metrics
 * without service bay management.
 * </p>
 */
public interface WorkshopOperationQueryService {
    
    /**
     * Get a workshop operation by its workshop ID
     */
    Optional<WorkshopOperation> handle(GetWorkshopOperationByWorkshopIdQuery query);
    
    /**
     * Get all workshop operations with their metrics
     */
    List<WorkshopOperation> handle(GetAllWorkshopsQuery query);
}
