package com.safecar.platform.workshop.application.internal.commandservices;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.aggregates.WorkshopOperation;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;
import com.safecar.platform.workshop.domain.services.WorkshopOperationCommandService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.WorkshopOperationRepository;

/**
 * Workshop Operation Command Service Implementation
 * <p>
 * Simplified implementation focused on core workshop metrics
 * without service bay management.
 * </p>
 */
@Service
public class WorkshopOperationCommandServiceImpl implements WorkshopOperationCommandService {

    private final WorkshopOperationRepository operationRepository;

    public WorkshopOperationCommandServiceImpl(WorkshopOperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    /**
     * Initialize workshop metrics for a new workshop
     * 
     * @param workshopId the workshop identifier
     * @return the initialized workshop operation if successful, empty otherwise
     */
    @Override
    public Optional<WorkshopOperation> initializeWorkshopMetrics(WorkshopId workshopId) {
        // Check if workshop already exists
        Optional<WorkshopOperation> existingOperation = operationRepository.findByWorkshop(workshopId);
        if (existingOperation.isPresent()) {
            return Optional.empty();
        }
        
        WorkshopOperation operation = new WorkshopOperation(workshopId, 0, 0);
        return Optional.of(operationRepository.save(operation));
    }

    /**
     * Increment appointment metrics for a workshop
     * 
     * @param workshopId the workshop identifier
     * @return the updated workshop operation if successful, empty otherwise
     */
    @Override
    public Optional<WorkshopOperation> incrementAppointmentMetrics(WorkshopId workshopId) {
        return updateWorkshopOperation(workshopId, WorkshopOperation::incrementAppointments);
    }

    /**
     * Increment service order metrics for a workshop
     * 
     * @param workshopId the workshop identifier
     * @return the updated workshop operation if successful, empty otherwise
     */
    @Override
    public Optional<WorkshopOperation> incrementServiceOrderMetrics(WorkshopId workshopId) {
        return updateWorkshopOperation(workshopId, WorkshopOperation::incrementServiceOrders);
    }

    /**
     * Complete a service order for a workshop
     * 
     * @param workshopId the workshop identifier
     * @return the updated workshop operation if successful, empty otherwise
     */
    @Override
    public Optional<WorkshopOperation> completeServiceOrder(WorkshopId workshopId) {
        return updateWorkshopOperation(workshopId, WorkshopOperation::completeServiceOrder);
    }

    /**
     * Helper method to update workshop operation with a given action
     */
    private Optional<WorkshopOperation> updateWorkshopOperation(WorkshopId workshopId, Consumer<WorkshopOperation> action) {
        Optional<WorkshopOperation> maybeOperation = operationRepository.findByWorkshop(workshopId);
        
        if (maybeOperation.isEmpty()) {
            return Optional.empty();
        }
        
        WorkshopOperation operation = maybeOperation.get();
        action.accept(operation);
        
        return Optional.of(operationRepository.save(operation));
    }
}
