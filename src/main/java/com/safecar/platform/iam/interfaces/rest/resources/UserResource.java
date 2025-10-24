package com.safecar.platform.iam.interfaces.rest.resources;

import java.util.List;
import java.util.UUID;

/**
 * Resource representing a user with their ID, username, and role.
 * <p>
 * Used as a response object for user-related endpoints.
 * </p>
 *
 * @param id       the unique identifier of the user
 * @param email    the email of the user
 * @param role     the role assigned to the user
 * @since 1.0.0
 * @author GonzaloQu3dena
 */
public record UserResource(UUID id, String email, List<String> roles) {
}