package com.safecar.platform.profiles.interfaces.rest.transform;

import com.safecar.platform.profiles.domain.model.commands.CreateWorkshopMechanicCommand;
import com.safecar.platform.profiles.interfaces.rest.resource.CreateWorkshopMechanicResource;

/**
 * Create Workshop Mechanic Command From Resource Assembler
 * <p>
 * Transforms a CreateWorkshopMechanicResource into a
 * CreateWorkshopMechanicCommand.
 * </p>
 */
public class CreateWorkshopMechanicCommandFromResourceAssembler {
    /**
     * Transforms a CreateWorkshopMechanicResource into a
     * CreateWorkshopMechanicCommand.
     * 
     * @param resource The resource containing the workshop mechanic details.
     * @return A command object for creating a workshop mechanic.
     */
    public static CreateWorkshopMechanicCommand toCommandFromResource(CreateWorkshopMechanicResource resource) {
        return new CreateWorkshopMechanicCommand(
                resource.fullName(),
                resource.city(),
                resource.country(),
                resource.phone(),
                resource.companyName(),
                resource.dni());
    }
}