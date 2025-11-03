package com.safecar.platform.workshopOps.interfaces.rest.transform;

import com.safecar.platform.workshopOps.domain.model.commands.CreateAppointmentCommand;
import com.safecar.platform.workshopOps.interfaces.rest.resources.CreateAppointmentResource;

/**
 * Assembler class for converting {@link CreateAppointmentResource} into {@link CreateAppointmentCommand}.
 */
public class CreateAppointmentCommandFromResourceAssembler {

    /**
     * Converts a {@link CreateAppointmentResource} into a {@link CreateAppointmentCommand}.
     *
     * @param resource the create appointment resource
     * @return the corresponding {@link CreateAppointmentCommand}
     */
    public static CreateAppointmentCommand toCommandFromResource(CreateAppointmentResource resource) {
        return new CreateAppointmentCommand(
                resource.code(),
                resource.scheduledDate(),
                resource.serviceType(),
                resource.description(),
                resource.customerId(),
                resource.vehicleId(),
                resource.workshopId()
        );
    }
}

