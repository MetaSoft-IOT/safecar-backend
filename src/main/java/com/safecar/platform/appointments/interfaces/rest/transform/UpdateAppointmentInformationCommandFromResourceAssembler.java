package com.safecar.platform.appointments.interfaces.rest.transform;

import com.safecar.platform.appointments.domain.model.commands.UpdateAppointmentInformationCommand;
import com.safecar.platform.appointments.interfaces.rest.resources.UpdateAppointmentInformationResource;

/**
 * Assembler class for converting {@link UpdateAppointmentInformationResource} into {@link UpdateAppointmentInformationCommand}.
 */
public class UpdateAppointmentInformationCommandFromResourceAssembler {

    /**
     * Converts a {@link UpdateAppointmentInformationResource} into a {@link UpdateAppointmentInformationCommand}.
     *
     * @param appointmentId the appointment identifier
     * @param resource the update appointment information resource
     * @return the corresponding {@link UpdateAppointmentInformationCommand}
     */
    public static UpdateAppointmentInformationCommand toCommandFromResource(java.util.UUID appointmentId, UpdateAppointmentInformationResource resource) {
        return new UpdateAppointmentInformationCommand(
                appointmentId,
                resource.serviceType(),
                resource.description()
        );
    }
}

