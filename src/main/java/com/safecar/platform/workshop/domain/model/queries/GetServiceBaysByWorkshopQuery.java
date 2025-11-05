package com.safecar.platform.workshop.domain.model.queries;

import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

/**
 * Query to retrieve service bays for a given workshop.
 */
public record GetServiceBaysByWorkshopQuery(
        WorkshopId workshopId
) {
}
