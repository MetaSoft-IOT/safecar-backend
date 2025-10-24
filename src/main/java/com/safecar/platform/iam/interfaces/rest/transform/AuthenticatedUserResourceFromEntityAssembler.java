package com.safecar.platform.iam.interfaces.rest.transform;

import com.safecar.platform.iam.domain.model.aggregates.User;
import com.safecar.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;

/**
 * Assembler class for converting {@link UserAggregate} entities and JWT tokens
 * into {@link AuthenticatedUserResource} objects.
 * <p>
 * Used to transform domain entities into resources suitable for API responses
 * that include authentication information.
 * </p>
 *
 * @author GonzaloQu3dena
 * @since 1.0.0
 */
public class AuthenticatedUserResourceFromEntityAssembler {

    /**
     * Converts a {@link UserAggregate} entity and a JWT token into an {@link AuthenticatedUserResource}.
     *
     * @param entity the user aggregate entity to convert
     * @param token  the JWT authentication token
     * @return the corresponding {@link AuthenticatedUserResource}
     */
    public static AuthenticatedUserResource toResourceFromEntity(User entity, String token) {
        return new AuthenticatedUserResource(
                entity.getId(),
                entity.getEmail(),
                token
        );
    }
}