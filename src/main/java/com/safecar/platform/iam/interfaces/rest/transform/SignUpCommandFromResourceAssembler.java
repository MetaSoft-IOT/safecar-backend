package com.safecar.platform.iam.interfaces.rest.transform;

import com.safecar.platform.iam.domain.model.commands.SignUpCommand;
import com.safecar.platform.iam.interfaces.rest.resources.SignUpResource;

/**
 * Assembler class for converting {@link SignUpResource} objects into {@link SignUpCommand} commands.
 * <p>
 * Used to transform API request resources into domain commands for user registration operations.
 * </p>
 *
 * @author GonzaloQu3dena
 * @since 1.0.0
 */
public class SignUpCommandFromResourceAssembler {

    /**
     * Converts a {@link SignUpResource} into a {@link SignUpCommand}.
     *
     * @param resource the sign-up resource containing user registration data
     * @return a new {@link SignUpCommand} constructed from the provided resource
     */
    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        var roles = RoleListFromStringAssembler.toRoleListFromStringList(resource.roles());
        System.out.println("roles: " + roles);
        return new SignUpCommand(resource.email(), resource.password(), resource.confirmPassword(), roles);
    }
}