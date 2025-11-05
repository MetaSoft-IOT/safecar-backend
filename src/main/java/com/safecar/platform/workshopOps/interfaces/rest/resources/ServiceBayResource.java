package com.safecar.platform.workshopOps.interfaces.rest.resources;

public record ServiceBayResource(
        Long id,
        String label,
        Long workshopId
) {
}
