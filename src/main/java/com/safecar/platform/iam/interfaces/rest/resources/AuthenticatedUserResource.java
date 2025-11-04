package com.safecar.platform.iam.interfaces.rest.resources;

/**
 * Resource representing an authenticated user with their ID, username, and authentication token.
 * <p>
 * Used as a response object after successful authentication.
 * </p>
 *
 * @param id       the unique identifier of the user
 * @param username the username of the authenticated user
 * @param token    the authentication token issued to the user
 * @author GonzaloQu3dena
 * @since 1.0.0
 */
public record AuthenticatedUserResource(Long id, String username, String token) {
}