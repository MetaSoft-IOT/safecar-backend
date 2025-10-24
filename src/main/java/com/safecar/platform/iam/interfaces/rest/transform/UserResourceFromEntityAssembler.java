package com.safecar.platform.iam.interfaces.rest.transform;

import com.safecar.platform.iam.domain.model.aggregates.User;
import com.safecar.platform.iam.interfaces.rest.resources.UserResource;

/**
 * Assembler class for converting {@link UserAggregate} entities into {@link UserResource} objects.
 * <p>
 * Used to transform domain user aggregates into resources suitable for API responses.
 * </p>
 *
 * @author GonzaloQu3dena
 * @since 1.0.0
 */
public class UserResourceFromEntityAssembler {

    /**
     * Converts a {@link UserAggregate} entity into a {@link UserResource}.
     *
     * @param entity the user aggregate entity to convert
     * @return the corresponding {@link UserResource}
     */
    public static UserResource toResourceFromEntity(User entity) {
        return new UserResource(
                entity.getId(),
                entity.getEmail(),
                RoleStringListFromEntityListAssembler.toResourceListFromEntitySet(entity.getRoles())
        );
    }
}