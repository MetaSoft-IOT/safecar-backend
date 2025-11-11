package com.safecar.platform.workshop.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Resource for creating a new workshop operation
 */
public record CreateWorkshopOperationResource(
        @NotNull(message = "Workshop ID is required")
        @Positive(message = "Workshop ID must be positive")
        Long workshopId
) {
}