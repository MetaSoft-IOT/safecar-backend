package com.safecar.platform.iam.domain.model.commands;

import java.util.List;
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
        String username, String password, List<Role> roles
) {
}