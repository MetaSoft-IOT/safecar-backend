package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.aggregates.ServiceOrder;
import com.safecar.platform.workshop.interfaces.rest.resources.ServiceOrderResource;

/**
 * Service Order Resource From Aggregate Assembler - Assembler to convert
 * ServiceOrder aggregate into ServiceOrderResource
 */
public class ServiceOrderResourceFromAggregateAssembler {

    /**
     * Convert ServiceOrder aggregate to ServiceOrderResource.
     *
     * @param aggregate ServiceOrder aggregate
     * @return ServiceOrderResource
     */
    public static ServiceOrderResource toResourceFromAggregate(ServiceOrder aggregate) {
        if (aggregate == null)
            return null;

        var code = aggregate.getCode() != null ? aggregate.getCode().value() : null;
        var workshopId = aggregate.getWorkshop() != null ? aggregate.getWorkshop().workshopId() : null;
        var vehicleId = aggregate.getVehicle() != null ? aggregate.getVehicle().vehicleId() : null;
        var driverId = aggregate.getDriver() != null ? aggregate.getDriver().driverId() : null;

        return new ServiceOrderResource(
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
