package com.safecar.platform.workshop.domain.services;

import java.util.List;
import java.util.Optional;

import com.safecar.platform.workshop.domain.model.aggregates.ServiceOrder;
import com.safecar.platform.workshop.domain.model.queries.GetServiceOrderByIdQuery;
import com.safecar.platform.workshop.domain.model.queries.GetServiceOrdersByWorkshopQuery;

/**
 * Service Order Query Service - handles queries related to service orders.
 */
public interface ServiceOrderQueryService {
    /**
     * Handles getting a service order by id.
     * @param query the query to get a service order by id
     * @return an optional service order
     */
    Optional<ServiceOrder> handle(GetServiceOrderByIdQuery query);
    /**
     * Handles getting service orders by workshop, optionally filtered by status.
     * @param query the query to get service orders by workshop
     * @return a list of service orders
     */
    List<ServiceOrder> handle(GetServiceOrdersByWorkshopQuery query);
}
