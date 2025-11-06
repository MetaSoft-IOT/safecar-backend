package com.safecar.platform.workshop.interfaces.rest.resources;

public record AllocateServiceBayResource(
        Long workshopId,
        String label
) {
}
