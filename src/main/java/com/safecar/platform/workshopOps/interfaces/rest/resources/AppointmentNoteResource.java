package com.safecar.platform.workshopOps.interfaces.rest.resources;

import java.util.Date;
import java.util.UUID;

/**
 * Resource representing an appointment note.
 *
 * @param id the unique identifier of the note
 * @param content the note content
 * @param authorId the author identifier
 * @param createdAt the creation timestamp
 */
public record AppointmentNoteResource(
        Long id,
        String content,
        UUID authorId,
        Date createdAt
) {
}

