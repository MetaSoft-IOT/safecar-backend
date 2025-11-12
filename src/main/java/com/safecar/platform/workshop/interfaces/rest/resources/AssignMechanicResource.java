package com.safecar.platform.workshop.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

/**
 * Assign Mechanic Resource - Represents the raw data required to assign a mechanic
 * to a task. Validation will be performed when mapping to domain commands.
 * 
 * @param mechanicId The unique identifier of the mechanic to be assigned.
 */
public record AssignMechanicResource(Long mechanicId) {
}
