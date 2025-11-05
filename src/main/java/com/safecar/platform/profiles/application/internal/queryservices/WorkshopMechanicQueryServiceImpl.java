package com.safecar.platform.profiles.application.internal.queryservices;

import com.safecar.platform.profiles.domain.model.aggregates.WorkshopMechanic;
import com.safecar.platform.profiles.domain.model.queries.GetWorkshopMechanicByUserIdQuery;
import com.safecar.platform.profiles.domain.services.WorkshopMechanicQueryService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.WorkshopMechanicRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkshopMechanicQueryServiceImpl implements WorkshopMechanicQueryService {
    private final WorkshopMechanicRepository workshopMechanicRepository;

    public WorkshopMechanicQueryServiceImpl(WorkshopMechanicRepository workshopMechanicRepository) {
        this.workshopMechanicRepository = workshopMechanicRepository;
    }

    @Override
    public Optional<WorkshopMechanic> handle(GetWorkshopMechanicByUserIdQuery query) {
        return workshopMechanicRepository.findWorkshopMechanicByUserId(query.userId());
    }
}
