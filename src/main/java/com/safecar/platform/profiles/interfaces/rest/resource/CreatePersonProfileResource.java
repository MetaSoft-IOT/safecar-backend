package com.safecar.platform.profiles.interfaces.rest.resource;

/**
 * Create Person Profile Resource
 */
public record CreatePersonProfileResource(
        String fullName,
        String city,
        String country,
        String phone,
        String dni,
        String companyName // optional for workshop flows; ignored by PersonProfile
) {
}
