package com.safecar.platform.profiles.domain.services;

import com.safecar.platform.profiles.domain.model.aggregates.WorkshopMechanic;
import com.safecar.platform.profiles.domain.model.queries.GetWorkshopMechanicByUserIdQuery;

import java.util.Optional;

/**
 * Workshop Mechanic Query Service Interface
 * <p>
 * This interface defines the contract for querying WorkshopMechanic entities
 * based on user ID.
 * </p>
 */
public interface WorkshopMechanicQueryService {
    /**
     * Handles the query to retrieve a WorkshopMechanic by user ID.
     *
     * @param query the query containing the user ID
     * @return an Optional containing the WorkshopMechanic if found, otherwise empty
     */
    Optional<WorkshopMechanic> handle(GetWorkshopMechanicByUserIdQuery query);
}
