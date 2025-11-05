package com.safecar.platform.workshopOps.interfaces.rest.transform;

import com.safecar.platform.workshopOps.domain.model.commands.AddAppointmentNoteCommand;
import com.safecar.platform.workshopOps.interfaces.rest.resources.AddAppointmentNoteResource;

/**
 * Add Appointment Note Command From Resource Assembler - Transforms the add
 * appointment note resource to a command.
 */
public class AddAppointmentNoteCommandFromResourceAssembler {

    /**
     * Converts a {@link AddAppointmentNoteResource} to a
     * {@link AddAppointmentNoteCommand}.
     *
     * @param appointmentId the appointment ID
     * @param resource      the add appointment note resource
     * @return the add appointment note command
     */
    public static AddAppointmentNoteCommand toCommandFromResource(Long appointmentId,
            AddAppointmentNoteResource resource) {
        return new AddAppointmentNoteCommand(
                appointmentId,
                resource.content(),
                resource.authorId());
    }
}
