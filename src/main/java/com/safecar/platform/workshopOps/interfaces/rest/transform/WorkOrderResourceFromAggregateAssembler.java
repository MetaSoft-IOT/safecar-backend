package com.safecar.platform.workshopOps.interfaces.rest.transform;

import com.safecar.platform.workshopOps.domain.model.aggregates.WorkshopOrder;
import com.safecar.platform.workshopOps.interfaces.rest.resources.WorkOrderResource;

/**
 * Work Order Resource From Aggregate Assembler - Assembler to convert
 * WorkshopOrder aggregate into WorkOrderResource
 */
public class WorkOrderResourceFromAggregateAssembler {

    /**
     * Convert WorkshopOrder aggregate to WorkOrderResource.
     *
     * @param aggregate WorkshopOrder aggregate
     * @return WorkOrderResource
     */
    public static WorkOrderResource toResourceFromAggregate(WorkshopOrder aggregate) {
        if (aggregate == null)
            return null;

        var code = aggregate.getCode() != null ? aggregate.getCode().value() : null;
        var workshopId = aggregate.getWorkshop() != null ? aggregate.getWorkshop().workshopId() : null;
        var vehicleId = aggregate.getVehicle() != null ? aggregate.getVehicle().vehicleId() : null;
        var driverId = aggregate.getDriver() != null ? aggregate.getDriver().driverId() : null;

        return new WorkOrderResource(
                aggregate.getId(),
                code,
                workshopId,
                vehicleId,
                driverId,
                aggregate.getOpenedAt(),
                aggregate.getClosedAt(),
                aggregate.getStatus() != null ? aggregate.getStatus().name() : null);
    }
}
