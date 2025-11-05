package com.safecar.platform.workshop.domain.model.queries;

import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

/**
 * Query to retrieve a workshop operation by id.
 */
public record GetWorkshopByIdQuery(
        WorkshopId workshopId
) {
}
