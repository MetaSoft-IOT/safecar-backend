package com.safecar.platform.profiles.interfaces.rest.transform;

import com.safecar.platform.profiles.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.profiles.interfaces.rest.resource.CreateDriverResource;

/**
 * Assembler to convert CreateDriverResource to CreateDriverCommand.
 * 
 * This class provides transformation methods to convert REST API resources
 * into domain commands for driver profile creation operations.
 */
public class CreateDriverCommandFromResourceAssembler {

    /**
     * Converts a CreateDriverResource to a CreateDriverCommand.
     *
     * @param resource the create driver resource from the REST API
     * @return the corresponding create driver command for the domain layer
     */
    public static CreateDriverCommand toCommandFromResource(CreateDriverResource resource) {
        return new CreateDriverCommand(
            resource.fullName(),
            resource.city(),
            resource.country(),
            resource.phone(),
            resource.dni()
        );
    }
}