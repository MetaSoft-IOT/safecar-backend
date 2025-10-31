package com.safecar.platform.profiles.interfaces.rest.resource;

public record WorkshopResource(
        Long userId,
        Long workshopId,
        String fullName,
        String city,
        String country,
        String phone,
        String companyName,
        String dni
) {
}
