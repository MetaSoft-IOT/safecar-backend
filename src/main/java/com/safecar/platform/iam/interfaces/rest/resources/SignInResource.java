package com.safecar.platform.iam.interfaces.rest.resources;

/**
 * Resource representing the credentials required for user sign-in.
 * <p>
 * Used as a request object for authentication endpoints.
 * </p>
 *
 * @param email the email of the user attempting to sign in
 * @param password the password of the user attempting to sign in
 * @author GonzaloQu3dena
 * @since 1.0.0
 */
public record SignInResource(String email, String password) {
}