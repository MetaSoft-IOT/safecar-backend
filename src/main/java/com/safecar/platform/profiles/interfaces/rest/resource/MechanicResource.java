package com.safecar.platform.profiles.interfaces.rest.resource;

import java.util.UUID;

public record MechanicResource(
        UUID userId,
        UUID MechanicId,
        String fullName,
        String city,
        String country,
        String phone,
        String companyName,
        String dni
) {
}
