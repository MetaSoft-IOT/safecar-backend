package com.safecar.platform.profiles.domain.model.queries;

/**
 * Get workshop mechanic by user ID query.
 * <p>
 * This query is used to retrieve a workshop mechanic's details based on the
 * provided user ID.
 * </p>
 * 
 * @param userId the ID of the user associated with the workshop mechanic
 */
public record GetWorkshopMechanicByUserIdQuery(Long userId) {
}