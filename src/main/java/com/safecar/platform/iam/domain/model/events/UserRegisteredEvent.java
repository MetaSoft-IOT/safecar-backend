package com.safecar.platform.iam.domain.model.events;

import java.util.UUID;

/**
 * Domain event representing the registration of a new user.
 * <p>
 * Contains the unique identifier of the registered user.
 *
 * @param userId the unique identifier of the registered user
 * 
 * @author GonzaloQu3dena
 * @since 2025-10-06
 * @version 1.0.0
 */
public record UserRegisteredEvent(
        Long userId
) {
}