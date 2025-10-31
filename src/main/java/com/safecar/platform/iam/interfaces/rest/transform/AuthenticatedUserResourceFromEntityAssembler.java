package com.safecar.platform.iam.interfaces.rest.transform;


import com.safecar.platform.iam.domain.model.aggregates.User;
import com.safecar.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(user.getId(), user.getEmail(), token, user.getSerializedRoles());
    }
}
