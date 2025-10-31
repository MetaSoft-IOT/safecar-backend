package com.safecar.platform.iam.interfaces.rest.resources;

import java.util.List;
import java.util.UUID;

public record UserResource(
        Long id,
        String email,
        List<String> roles
) {
}
