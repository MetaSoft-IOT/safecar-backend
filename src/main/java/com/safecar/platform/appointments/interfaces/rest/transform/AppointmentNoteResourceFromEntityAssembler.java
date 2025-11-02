package com.safecar.platform.appointments.interfaces.rest.transform;

import com.safecar.platform.appointments.domain.model.entities.AppointmentNote;
import com.safecar.platform.appointments.interfaces.rest.resources.AppointmentNoteResource;

/**
 * Assembler class for converting {@link AppointmentNote} entities into {@link AppointmentNoteResource} objects.
 */
public class AppointmentNoteResourceFromEntityAssembler {

    /**
     * Converts an {@link AppointmentNote} entity into an {@link AppointmentNoteResource}.
     *
     * @param entity the appointment note entity to convert
     * @return the corresponding {@link AppointmentNoteResource}
     */
    public static AppointmentNoteResource toResourceFromEntity(AppointmentNote entity) {
        return new AppointmentNoteResource(
                entity.getId(),
                entity.getContent(),
                entity.getAuthorId(),
                entity.getCreatedAt()
        );
    }
}

