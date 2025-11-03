package com.safecar.platform.workshopOps.interfaces.rest.transform;

import com.safecar.platform.workshopOps.domain.model.commands.AssignMechanicCommand;
import com.safecar.platform.workshopOps.interfaces.rest.resources.AssignMechanicResource;

/**
 * Assembler class for converting {@link AssignMechanicResource} into {@link AssignMechanicCommand}.
 */
public class AssignMechanicCommandFromResourceAssembler {

    /**
     * Converts an {@link AssignMechanicResource} into an {@link AssignMechanicCommand}.
     *
     * @param appointmentId the appointment identifier
     * @param resource the assign mechanic resource
     * @return the corresponding {@link AssignMechanicCommand}
     */
    public static AssignMechanicCommand toCommandFromResource(java.util.UUID appointmentId, AssignMechanicResource resource) {
        return new AssignMechanicCommand(
                appointmentId,
                resource.mechanicId()
        );
    }
}

