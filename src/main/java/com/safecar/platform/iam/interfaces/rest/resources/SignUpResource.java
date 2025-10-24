package com.safecar.platform.iam.interfaces.rest.resources;

import java.util.Set;

/**
 * Resource representing the information required for user sign-up.
 * <p>
 * Used as a request object for registration endpoints.
 * </p>
 *
 * @param email the email chosen by the user during sign-up
 * @param password the password chosen by the user during sign-up
 * @param role     the role assigned to the user during sign-up
 * @author GonzaloQu3dena
 * @since 1.0.0
 */
public record SignUpResource(
        String email,
        String password,
        String confirmPassword,
        Set<String> roles
) {
}