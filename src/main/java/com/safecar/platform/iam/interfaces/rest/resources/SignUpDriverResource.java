package com.safecar.platform.iam.interfaces.rest.resources;

public record SignUpDriverResource(
        String email,
        String password,
        String fullName,
        String city,
        String country,
        String phone,
        String dni
) {
}
