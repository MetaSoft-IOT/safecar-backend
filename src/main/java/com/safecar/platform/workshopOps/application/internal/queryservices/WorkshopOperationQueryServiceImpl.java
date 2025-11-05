package com.safecar.platform.workshopOps.application.internal.queryservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshopOps.domain.model.aggregates.WorkshopOperation;
import com.safecar.platform.workshopOps.domain.model.entities.ServiceBay;
import com.safecar.platform.workshopOps.domain.model.queries.GetServiceBaysByWorkshopQuery;
import com.safecar.platform.workshopOps.domain.model.queries.GetWorkshopByIdQuery;
import com.safecar.platform.workshopOps.domain.services.WorkshopOperationQueryService;
import com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories.ServiceBayRepository;
import com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories.WorkshopOperationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WorkshopOperationQueryServiceImpl implements WorkshopOperationQueryService {

    private final WorkshopOperationRepository operationRepository;
    private final ServiceBayRepository serviceBayRepository;

    public WorkshopOperationQueryServiceImpl(WorkshopOperationRepository operationRepository, ServiceBayRepository serviceBayRepository) {
        this.operationRepository = operationRepository;
        this.serviceBayRepository = serviceBayRepository;
    }

    @Override
    public Optional<WorkshopOperation> handle(GetWorkshopByIdQuery query) {
        return operationRepository.findByWorkshop(query.workshopId());
    }

    @Override
    public List<ServiceBay> handle(GetServiceBaysByWorkshopQuery query) {
        return serviceBayRepository.findByWorkshop(query.workshopId());
    }
}
