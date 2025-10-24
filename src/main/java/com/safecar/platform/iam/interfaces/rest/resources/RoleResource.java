package com.safecar.platform.iam.interfaces.rest.resources;

/**
 * Resource representing a role with its ID and name.
 * <p>
 * Used as a response object for role-related endpoints.
 * </p>
 *
 * @param id   the unique identifier of the role
 * @param name the name of the role
 * @author GonzaloQu3dena
 * @since 1.0.0
 */
public record RoleResource(Long id, String name) {
}