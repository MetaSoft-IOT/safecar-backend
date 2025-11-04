package com.safecar.platform.workshopOps.domain.model.queries;

import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkshopId;

/**
 * Query to retrieve service bays for a given workshop.
 */
public record GetServiceBaysByWorkshopQuery(
        WorkshopId workshopId
) {
}
