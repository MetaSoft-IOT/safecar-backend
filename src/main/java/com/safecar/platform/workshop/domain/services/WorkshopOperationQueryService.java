package com.safecar.platform.workshop.domain.services;

import java.util.List;
import java.util.Optional;

import com.safecar.platform.workshop.domain.model.aggregates.WorkshopOperation;
import com.safecar.platform.workshop.domain.model.entities.ServiceBay;
import com.safecar.platform.workshop.domain.model.queries.GetServiceBaysByWorkshopQuery;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByIdQuery;

public interface WorkshopOperationQueryService {
    Optional<WorkshopOperation> handle(GetWorkshopByIdQuery query);
    List<ServiceBay> handle(GetServiceBaysByWorkshopQuery query);
}
