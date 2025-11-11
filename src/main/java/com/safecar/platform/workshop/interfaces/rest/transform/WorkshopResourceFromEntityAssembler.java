package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.aggregates.WorkshopOperation;
import com.safecar.platform.workshop.interfaces.rest.resources.WorkshopOperationResource;

/**
 * Assembler to transform Workshop Operation Entity to Workshop Resource
 */
public class WorkshopResourceFromEntityAssembler {

    /**
     * Transform WorkshopOperation entity to WorkshopResource
     *
     * @param entity the WorkshopOperation entity
     * @return the WorkshopResource
     */
    public static WorkshopOperationResource toResourceFromEntity(WorkshopOperation entity) {
        return new WorkshopOperationResource(
                entity.getId(),
                entity.getWorkshop().workshopId(),
                entity.getTotalAppointments(),
                entity.getTotalServiceOrders(),
                entity.getActiveOperations(),
                entity.getEfficiencyRatio()
        );
    }
}