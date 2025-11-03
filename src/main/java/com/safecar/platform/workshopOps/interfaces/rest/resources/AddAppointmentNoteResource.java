package com.safecar.platform.workshopOps.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Resource for adding a note to an appointment.
 *
 * @param content the note content
 * @param authorId the author identifier
 */
public record AddAppointmentNoteResource(
        @NotBlank(message = "Note content is required")
        String content,

        @NotNull(message = "Author ID is required")
        UUID authorId
) {
}

