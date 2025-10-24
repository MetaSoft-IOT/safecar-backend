package com.safecar.platform.iam.domain.model.commands;

import java.util.Set;
import com.safecar.platform.iam.domain.model.entities.Role;

/**
 * SignUpCommand
 * <p>
 * Command record for user sign-up containing email, password, confirm password,
 * and role.
 * </p>
 * 
 * @author GonzaloQu3dena
 * @version 1.0
 * @since 2025-10-05
 */
public record SignUpCommand(
        String email,
        String password,
        String confirmPassword,
        Set<Role> roles) {
    public SignUpCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        if (confirmPassword == null || confirmPassword.isBlank()) {
            throw new IllegalArgumentException("Confirm Password cannot be null or blank");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Password and Confirm Password do not match");
        }
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Roles cannot be null or empty");
        }
    }

}
