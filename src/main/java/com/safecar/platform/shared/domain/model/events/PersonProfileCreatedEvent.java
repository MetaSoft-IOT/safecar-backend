package com.safecar.platform.shared.domain.model.events;

import java.util.Set;

/**
 * Person Profile Created Event
 * <p>
 * Event published when a new person profile is created.
 * </p>
 * 
 * @param profileId The unique identifier of the created person profile.
 * @param userRoles The roles assigned to the user associated with the profile.
 */
public record PersonProfileCreatedEvent(
                Long profileId,
                Set<String> userRoles) {
}
