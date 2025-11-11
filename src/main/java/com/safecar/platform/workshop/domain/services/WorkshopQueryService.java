package com.safecar.platform.workshop.domain.services;

import com.safecar.platform.workshop.domain.model.aggregates.Workshop;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByIdQuery;

import java.util.Optional;

/**
 * Workshop Query Service
 */
public interface WorkshopQueryService {

    /**
     * Handle the retrieval of a Workshop by its ID.
     * 
     * @param query the {@link GetWorkshopByIdQuery} instance
     * @return an {@link Optional} of {@link Workshop} if found
     */
    Optional<Workshop> handle(GetWorkshopByIdQuery query);
}