package com.safecar.platform.iam.interfaces.rest.transform;

import com.safecar.platform.iam.domain.model.commands.SignInCommand;
import com.safecar.platform.iam.interfaces.rest.resources.SignInResource;

/**
 * Assembler class for converting {@link SignInResource} objects into {@link SignInCommand} commands.
 * <p>
 * Used to transform API request resources into domain commands for sign-in operations.
 * </p>
 *
 * @author GonzaloQu3dena
 * @since 1.0.0
 */
public class SignInCommandFromResourceAssembler {

    /**
     * Converts a {@link SignInResource} into a {@link SignInCommand}.
     *
     * @param resource the sign-in resource containing user credentials
     * @return a {@link SignInCommand} with the provided username and password
     */
    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(
                resource.email(),
                resource.password());
    }
}
