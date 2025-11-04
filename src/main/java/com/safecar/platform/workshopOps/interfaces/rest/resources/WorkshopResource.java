package com.safecar.platform.workshopOps.interfaces.rest.resources;

public record WorkshopResource(
        Long id,
        int mechanicsCount,
        int baysCount
) {
}
