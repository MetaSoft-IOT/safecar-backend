package com.safecar.platform.workshop.interfaces.rest.resources;

public record ServiceBayResource(
        Long id,
        String label,
        Long workshopId
) {
}
