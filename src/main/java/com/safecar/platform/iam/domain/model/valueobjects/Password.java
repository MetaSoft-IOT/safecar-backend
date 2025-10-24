package com.safecar.platform.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value Object representing a Password with validation rules.
 * <p>
 *  A valid password must be at least 8 characters long and contain at least one symbol.
 * </p>
 * 
 * @throws IllegalArgumentException if the password does not meet the criteria.
 * 
 * @author GonzaloQu3dena
 * @version 1.0
 * @since 2025-10-05
 */
@Embeddable
public record Password(String value) {
    public Password {
        if (value == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        if (value.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        if (!value.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            throw new IllegalArgumentException(
                    "Password must contain at least one symbol (!@#$%^&*()_+-=[]{}|;':\"\\,.<>/?)");
        }
    }
}
