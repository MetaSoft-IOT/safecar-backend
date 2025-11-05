package com.safecar.platform.workshopOps.interfaces.rest.transform;

import com.safecar.platform.workshopOps.domain.model.commands.AllocateServiceBayCommand;
import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkshopId;
import com.safecar.platform.workshopOps.interfaces.rest.resources.AllocateServiceBayResource;

public class AllocateServiceBayCommandFromResourceAssembler {

    public static AllocateServiceBayCommand toCommandFromResource(AllocateServiceBayResource resource) {
        var workshop = new WorkshopId(resource.workshopId(), "Workshop " + resource.workshopId());
        return new AllocateServiceBayCommand(workshop, resource.label());
    }
}
