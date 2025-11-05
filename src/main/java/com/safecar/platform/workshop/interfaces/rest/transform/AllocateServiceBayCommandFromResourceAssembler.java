package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.commands.AllocateServiceBayCommand;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;
import com.safecar.platform.workshop.interfaces.rest.resources.AllocateServiceBayResource;

public class AllocateServiceBayCommandFromResourceAssembler {

    public static AllocateServiceBayCommand toCommandFromResource(AllocateServiceBayResource resource) {
        var workshop = new WorkshopId(resource.workshopId(), "Workshop " + resource.workshopId());
        return new AllocateServiceBayCommand(workshop, resource.label());
    }
}
