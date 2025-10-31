package com.safecar.platform.profiles.interfaces.rest.transform;



import com.safecar.platform.profiles.domain.model.aggregates.Workshop;
import com.safecar.platform.profiles.interfaces.rest.resource.WorkshopResource;

public class WorkshopResourceFromEntityAssembler {
    public static WorkshopResource toResourceFromEntity(Workshop workshop) {
        return new WorkshopResource(
                workshop.getUserId(), workshop.getId(), workshop.getFullName(), workshop.getCity(),
                workshop.getCountry(), workshop.getPhone(), workshop.getCompanyName(), workshop.getDni()
        );
    }
}
