package com.safecar.platform.profiles.interfaces.rest.transform;



import com.safecar.platform.profiles.domain.model.aggregates.Mechanic;
import com.safecar.platform.profiles.interfaces.rest.resource.MechanicResource;

public class MechanicResourceFromEntityAssembler {
    public static MechanicResource toResourceFromEntity(Mechanic mechanic) {
        return new MechanicResource(
                mechanic.getUserId(), mechanic.getId(), mechanic.getFullName(), mechanic.getCity(),
                mechanic.getCountry(), mechanic.getPhone(), mechanic.getCompanyName(), mechanic.getDni()
        );
    }
}
