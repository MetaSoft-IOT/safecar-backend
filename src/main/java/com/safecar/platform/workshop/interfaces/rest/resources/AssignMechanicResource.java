package com.safecar.platform.workshop.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Assign Mechanic Resource - Represents the data required to assign a mechanic
 * to a task.
 * 
 * @param mechanicId The unique identifier of the mechanic to be assigned.
 */
public record AssignMechanicResource(
                @NotNull(message = "Mechanic ID is required") UUID mechanicId) {
}
