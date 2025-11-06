package com.safecar.platform.profiles.interfaces.rest.transform;

import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.interfaces.rest.resource.PersonProfileResource;

public class PersonProfileResourceFromEntityAssembler {
    public static PersonProfileResource toResourceFromEntity(PersonProfile p) {
        String phone = p.getPhone() != null ? p.getPhone().phone() : null;
        String dni = p.getDni() != null ? p.getDni().dni() : null;
        return new PersonProfileResource(p.getUserId(), p.getId(), p.getFullName(), p.getCity(), p.getCountry(), phone, dni);
    }
}
