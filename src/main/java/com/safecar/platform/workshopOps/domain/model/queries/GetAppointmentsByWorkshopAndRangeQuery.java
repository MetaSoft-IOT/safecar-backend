package com.safecar.platform.workshopOps.domain.model.queries;

import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkshopId;
import java.time.Instant;

/**
 * Query to get appointments by workshop and time range.
 */
public record GetAppointmentsByWorkshopAndRangeQuery(
        WorkshopId workshopId,
        Instant from,
        Instant to
) {
}