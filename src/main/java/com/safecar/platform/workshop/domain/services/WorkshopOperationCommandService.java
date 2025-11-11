package com.safecar.platform.workshop.domain.services;

import java.util.Optional;

import com.safecar.platform.workshop.domain.model.aggregates.WorkshopOperation;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

/**
 * Workshop Operation Command Service
 * <p>
 * Simplified service focused on core workshop metrics and operations
 * without physical service bay management.
 * </p>
 */
public interface WorkshopOperationCommandService {
    
    /**
     * Initializes or retrieves workshop operational metrics
     * 
     * @param workshopId Workshop identifier
     * @return Workshop operation data
     */
    Optional<WorkshopOperation> initializeWorkshopMetrics(WorkshopId workshopId);
    
    /**
     * Updates appointment metrics for workshop
     * 
     * @param workshopId Workshop identifier
     * @return Updated workshop operation
     */
    Optional<WorkshopOperation> incrementAppointmentMetrics(WorkshopId workshopId);
    
    /**
     * Updates service order metrics for workshop
     * 
     * @param workshopId Workshop identifier
     * @return Updated workshop operation
     */
    Optional<WorkshopOperation> incrementServiceOrderMetrics(WorkshopId workshopId);
    
    /**
     * Completes a service order and updates metrics
     * 
     * @param workshopId Workshop identifier
     * @return Updated workshop operation
     */
    Optional<WorkshopOperation> completeServiceOrder(WorkshopId workshopId);
}
