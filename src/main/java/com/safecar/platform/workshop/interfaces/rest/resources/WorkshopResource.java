package com.safecar.platform.workshop.interfaces.rest.resources;

public record WorkshopResource(
        Long id,
        int mechanicsCount,
        int baysCount
) {
}
