package com.safecar.platform.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value Object representing an Email
 * <p>
 * Ensures the email format is valid upon creation.
 * </p>
 * 
 * @param value the email address
 * @throws IllegalArgumentException if the email format is invalid
 * 
 * @author GonzaloQu3dena
 * @version 1.0
 * @since 2025-10-05
 */
@Embeddable
public record Email(String value) {
    public Email {
        if (value == null || !value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}
