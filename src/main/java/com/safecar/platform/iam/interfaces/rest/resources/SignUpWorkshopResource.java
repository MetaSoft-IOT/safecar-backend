package com.safecar.platform.iam.interfaces.rest.resources;

public record   SignUpWorkshopResource(
        String email,
        String password,
        String fullName,
        String city,
        String country,
        String phone,
        String companyName,
        String ruc
) {
}
