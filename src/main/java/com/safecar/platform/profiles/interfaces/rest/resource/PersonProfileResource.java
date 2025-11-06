package com.safecar.platform.profiles.interfaces.rest.resource;

/**
 * PersonProfile Resource
 */
public record PersonProfileResource(
        Long userId,
        Long profileId,
        String fullName,
        String city,
        String country,
        String phone,
        String dni) {
}
