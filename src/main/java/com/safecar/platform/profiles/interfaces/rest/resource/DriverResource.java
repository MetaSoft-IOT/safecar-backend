package com.safecar.platform.profiles.interfaces.rest.resource;

import java.util.UUID;

public record DriverResource(
        Long userId,
        Long driverId,
        String fullName,
        String city,
        String country,
        String phone,
        String dni
) {
}
