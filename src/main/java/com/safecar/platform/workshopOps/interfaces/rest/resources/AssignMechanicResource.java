package com.safecar.platform.workshopOps.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Resource for assigning a mechanic to an appointment.
 *
 * @param mechanicId the mechanic identifier
 */
public record AssignMechanicResource(
        @NotNull(message = "Mechanic ID is required")
        UUID mechanicId
) {
}

