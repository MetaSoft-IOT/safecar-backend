package com.safecar.platform.workshopOps.domain.services;

import com.safecar.platform.workshopOps.domain.model.aggregates.WorkshopOperation;
import com.safecar.platform.workshopOps.domain.model.entities.ServiceBay;
import com.safecar.platform.workshopOps.domain.model.queries.GetServiceBaysByWorkshopQuery;
import com.safecar.platform.workshopOps.domain.model.queries.GetWorkshopByIdQuery;

import java.util.List;
import java.util.Optional;

public interface WorkshopOperationQueryService {
    Optional<WorkshopOperation> handle(GetWorkshopByIdQuery query);
    List<ServiceBay> handle(GetServiceBaysByWorkshopQuery query);
}
