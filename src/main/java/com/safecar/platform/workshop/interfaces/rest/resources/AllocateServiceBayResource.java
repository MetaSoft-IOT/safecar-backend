package com.safecar.platform.workshop.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record AllocateServiceBayResource(
        @NotNull(message = "Workshop ID is required") Long workshopId,
        @NotNull(message = "Label is required") String label
) {
}
