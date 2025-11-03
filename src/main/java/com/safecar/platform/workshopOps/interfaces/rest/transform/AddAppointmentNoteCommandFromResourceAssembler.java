package com.safecar.platform.workshopOps.interfaces.rest.transform;

import com.safecar.platform.workshopOps.domain.model.commands.AddAppointmentNoteCommand;
import com.safecar.platform.workshopOps.interfaces.rest.resources.AddAppointmentNoteResource;

/**
 * Assembler class for converting {@link AddAppointmentNoteResource} into {@link AddAppointmentNoteCommand}.
 */
public class AddAppointmentNoteCommandFromResourceAssembler {

    /**
     * Converts an {@link AddAppointmentNoteResource} into an {@link AddAppointmentNoteCommand}.
     *
     * @param appointmentId the appointment identifier
     * @param resource the add appointment note resource
     * @return the corresponding {@link AddAppointmentNoteCommand}
     */
    public static AddAppointmentNoteCommand toCommandFromResource(java.util.UUID appointmentId, AddAppointmentNoteResource resource) {
        return new AddAppointmentNoteCommand(
                appointmentId,
                resource.content(),
                resource.authorId()
        );
    }
}

