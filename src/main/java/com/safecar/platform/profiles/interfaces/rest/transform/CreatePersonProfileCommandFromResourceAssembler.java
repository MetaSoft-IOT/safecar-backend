package com.safecar.platform.profiles.interfaces.rest.transform;

import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.safecar.platform.profiles.interfaces.rest.resource.CreatePersonProfileResource;

public class CreatePersonProfileCommandFromResourceAssembler {
    public static CreatePersonProfileCommand toCommandFromResource(CreatePersonProfileResource r) {
        return new CreatePersonProfileCommand(r.fullName(), r.city(), r.country(), r.phone(), r.dni());
    }
}
