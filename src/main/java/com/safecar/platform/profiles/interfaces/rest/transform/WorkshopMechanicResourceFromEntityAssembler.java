package com.safecar.platform.profiles.interfaces.rest.transform;

import com.safecar.platform.profiles.domain.model.aggregates.WorkshopMechanic;
import com.safecar.platform.profiles.interfaces.rest.resource.WorkshopMechanicResource;

/**
 * Workshop Mechanic Resource From Entity Assembler
 * <p>
 * Transforms WorkshopMechanic domain entities into WorkshopMechanicResource
 * representations for RESTful APIs.
 * This assembler encapsulates the mapping logic, ensuring a clean separation
 * between the domain model and its REST representation.
 * </p>
 */
public class WorkshopMechanicResourceFromEntityAssembler {
    /**
     * Converts a WorkshopMechanic entity to a WorkshopMechanicResource.
     * 
     * @param workshopMechanic the WorkshopMechanic domain entity
     * @return the corresponding WorkshopMechanicResource
     */
    public static WorkshopMechanicResource toResourceFromEntity(WorkshopMechanic workshopMechanic) {
        return new WorkshopMechanicResource(
                workshopMechanic.getUserId(),
                workshopMechanic.getId(),
                workshopMechanic.getFullName(),
                workshopMechanic.getCity(),
                workshopMechanic.getCountry(),
                workshopMechanic.getPhone(),
                workshopMechanic.getCompanyName(),
                workshopMechanic.getDni());
    }
}
