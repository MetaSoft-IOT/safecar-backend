package com.safecar.platform.workshopOps.interfaces.rest.transform;

import com.safecar.platform.workshopOps.domain.model.entities.ServiceBay;
import com.safecar.platform.workshopOps.interfaces.rest.resources.ServiceBayResource;

public class ServiceBayResourceFromEntityAssembler {

    public static ServiceBayResource toResourceFromEntity(ServiceBay entity) {
        if (entity == null) return null;
        var workshopId = entity.getWorkshop() != null ? entity.getWorkshop().workshopId() : null;
        return new ServiceBayResource(entity.getId(), entity.getLabel(), workshopId);
    }
}
