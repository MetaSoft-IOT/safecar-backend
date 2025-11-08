package com.safecar.platform.profiles.domain.model.queries;

/**
 * Get Person Profile By User Id Query
 * <p>
 *  Query to retrieve a person profile based on the associated user ID.
 * </p>
 */
public record GetPersonProfileByUserIdQuery(Long userId) {
}
