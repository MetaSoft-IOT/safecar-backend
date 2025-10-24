package com.safecar.platform.iam.domain.model.queries;

/**
 * Query to retrieve a user by their email from the system.
 * <p>
 * Used to request a specific user based on the provided email.
 *
 * @param email the email of the user to retrieve
 * 
 * @author GonzaloQu3dena
 * @since 2025-10-06
 * @version 1.0.0
 */
public record GetUserByEmailQuery(String email) {
    public GetUserByEmailQuery {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email cannot be null or blank");
    }
}