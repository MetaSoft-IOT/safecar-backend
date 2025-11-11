package com.safecar.platform.workshop.application.internal.queryservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.aggregates.ServiceOrder;
import com.safecar.platform.workshop.domain.model.queries.*;
import com.safecar.platform.workshop.domain.services.ServiceOrderQueryService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.ServiceOrderRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service Order Query Service Implementation
 * 
 * @see ServiceOrderQueryService ServiceOrderQueryService for method details.
 */
@Service
public class ServiceOrderQueryServiceImpl implements ServiceOrderQueryService {

    /**
     * Repository for ServiceOrder to handle persistence operations.
     */
    private final ServiceOrderRepository repository;

    /**
     * Constructor for ServiceOrderQueryServiceImpl.
     * 
     * @param repository Repository for ServiceOrder to handle persistence
     *                   operations.
     */
    public ServiceOrderQueryServiceImpl(ServiceOrderRepository repository) {
        this.repository = repository;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ServiceOrder> handle(GetServiceOrderByIdQuery query) {
        return repository.findById(query.serviceOrderId());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<ServiceOrder> handle(GetServiceOrdersByWorkshopQuery query) {
        if (query.status() == null) 
            return repository.findByWorkshop(query.workshopId());

        

        return repository.findByWorkshopAndStatus(query.workshopId(), query.status());
    }
}
