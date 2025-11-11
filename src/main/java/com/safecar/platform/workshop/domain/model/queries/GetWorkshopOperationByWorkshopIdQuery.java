package com.safecar.platform.workshop.domain.model.queries;

import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

/**
 * Get Workshop Operation by Workshop ID Query
 * 
 * @param workshopId the workshop ID
 */
public record GetWorkshopOperationByWorkshopIdQuery(WorkshopId workshopId) {
}