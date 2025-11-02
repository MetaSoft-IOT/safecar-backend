package com.safecar.platform.appointments.interfaces.rest.transform;

import com.safecar.platform.appointments.domain.model.commands.RescheduleAppointmentCommand;
import com.safecar.platform.appointments.interfaces.rest.resources.RescheduleAppointmentResource;

/**
 * Assembler class for converting {@link RescheduleAppointmentResource} into {@link RescheduleAppointmentCommand}.
 */
public class RescheduleAppointmentCommandFromResourceAssembler {

    /**
     * Converts a {@link RescheduleAppointmentResource} into a {@link RescheduleAppointmentCommand}.
     *
     * @param appointmentId the appointment identifier
     * @param resource the reschedule appointment resource
     * @return the corresponding {@link RescheduleAppointmentCommand}
     */
    public static RescheduleAppointmentCommand toCommandFromResource(java.util.UUID appointmentId, RescheduleAppointmentResource resource) {
        return new RescheduleAppointmentCommand(
                appointmentId,
                resource.newScheduledDate()
        );
    }
}

