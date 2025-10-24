package com.safecar.platform.iam.domain.services;

import com.safecar.platform.iam.domain.model.commands.SeedRolesCommand;

/**
 * Service interface for handling role-related commands.
 * <p>
 * Provides a method to process commands for seeding roles into the system.
 *
 * @author GonzaloQu3dena
 * @since 2025-10-06
 * @version 1.0.0
 */
public interface RoleCommandService {

    /**
     * Handles the command to seed roles into the system.
     *
     * @param command the command containing the roles to seed
     */
    void handle(SeedRolesCommand command);
}