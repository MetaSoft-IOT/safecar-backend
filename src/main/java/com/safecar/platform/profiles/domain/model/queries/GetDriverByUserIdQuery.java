package com.safecar.platform.profiles.domain.model.queries;

/**
 * Get Driver By User Id Query
 * <p>
 * This record represents a query to retrieve a driver based on the associated
 * user ID.
 * It encapsulates the user ID as a parameter for the query.
 * </p>
 * 
 * @param userId the ID of the user whose driver information is to be retrieved
 */
public record GetDriverByUserIdQuery(Long userId) {
}