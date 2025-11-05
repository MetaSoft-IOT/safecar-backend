package com.safecar.platform.workshopOps.domain.model.queries;

import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkshopId;

/**
 * Query to retrieve a workshop operation by id.
 */
public record GetWorkshopByIdQuery(
        WorkshopId workshopId
) {
}
