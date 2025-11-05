package com.safecar.platform.workshopOps.interfaces.rest.transform;

import com.safecar.platform.workshopOps.domain.model.entities.AppointmentNote;
import com.safecar.platform.workshopOps.interfaces.rest.resources.AppointmentNoteResource;

import java.time.LocalDateTime;

/**
 * Appointment Note Resource From Entity Assembler - Converts AppointmentNote
 * entities to AppointmentNoteResource.
 */
public class AppointmentNoteResourceFromEntityAssembler {

    /**
     * Converts a {@link AppointmentNote} entity to an
     * {@link AppointmentNoteResource}.
     *
     * @param entity the appointment note entity
     * @return the appointment note resource
     */
    public static AppointmentNoteResource toResourceFromEntity(AppointmentNote entity) {
        return new AppointmentNoteResource(
                entity.getId(),
                entity.getContent(),
                entity.getAuthorId(),
                LocalDateTime.now(),
                LocalDateTime.now());
    }
}
