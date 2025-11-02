package com.safecar.platform.appointments.interfaces.rest.transform;

import com.safecar.platform.appointments.domain.model.commands.CancelAppointmentCommand;
import com.safecar.platform.appointments.interfaces.rest.resources.CancelAppointmentResource;

/**
 * Assembler class for converting {@link CancelAppointmentResource} into {@link CancelAppointmentCommand}.
 */
public class CancelAppointmentCommandFromResourceAssembler {

    /**
     * Converts a {@link CancelAppointmentResource} into a {@link CancelAppointmentCommand}.
     *
     * @param appointmentId the appointment identifier
     * @param resource the cancel appointment resource
     * @return the corresponding {@link CancelAppointmentCommand}
     */
    public static CancelAppointmentCommand toCommandFromResource(java.util.UUID appointmentId, CancelAppointmentResource resource) {
        return new CancelAppointmentCommand(
                appointmentId,
                resource.reason()
        );
    }
}

