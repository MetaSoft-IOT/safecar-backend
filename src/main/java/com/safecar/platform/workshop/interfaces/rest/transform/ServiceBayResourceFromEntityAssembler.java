package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.entities.ServiceBay;
import com.safecar.platform.workshop.interfaces.rest.resources.ServiceBayResource;

public class ServiceBayResourceFromEntityAssembler {

    public static ServiceBayResource toResourceFromEntity(ServiceBay entity) {
        if (entity == null) return null;
        var workshopId = entity.getWorkshop() != null ? entity.getWorkshop().workshopId() : null;
        return new ServiceBayResource(entity.getId(), entity.getLabel(), workshopId);
    }
}
