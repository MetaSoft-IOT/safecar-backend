package com.safecar.platform.workshop.domain.services;

import java.util.List;
import java.util.Optional;

import com.safecar.platform.workshop.domain.model.aggregates.WorkshopOrder;
import com.safecar.platform.workshop.domain.model.queries.GetWorkOrderByIdQuery;
import com.safecar.platform.workshop.domain.model.queries.GetWorkOrdersByWorkshopQuery;

/**
 * Workshop Order Query Service - handles queries related to workshop orders.
 */
public interface WorkshopOrderQueryService {
    /**
     * Handles getting a work order by id.
     * @param query the query to get a work order by id
     * @return an optional work order
     */
    Optional<WorkshopOrder> handle(GetWorkOrderByIdQuery query);
    /**
     * Handles getting work orders by workshop, optionally filtered by status.
     * @param query the query to get work orders by workshop
     * @return a list of work orders
     */
    List<WorkshopOrder> handle(GetWorkOrdersByWorkshopQuery query);
}
