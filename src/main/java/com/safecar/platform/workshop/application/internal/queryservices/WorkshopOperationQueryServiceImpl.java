package com.safecar.platform.workshop.application.internal.queryservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.aggregates.WorkshopOperation;
import com.safecar.platform.workshop.domain.model.entities.ServiceBay;
import com.safecar.platform.workshop.domain.model.queries.GetServiceBaysByWorkshopQuery;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByIdQuery;
import com.safecar.platform.workshop.domain.services.WorkshopOperationQueryService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.ServiceBayRepository;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.WorkshopOperationRepository;

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
