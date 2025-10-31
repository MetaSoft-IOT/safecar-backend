package com.safecar.platform.profiles.application.internal.queryservices;

import com.safecar.platform.profiles.domain.model.aggregates.Workshop;
import com.safecar.platform.profiles.domain.model.queries.GetWorkshopByUserIdAsyncQuery;
import com.safecar.platform.profiles.domain.services.WorkshopQueryService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.WorkshopRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkshopQueryServiceImpl implements WorkshopQueryService {
    private final WorkshopRepository workshopRepository;

    public WorkshopQueryServiceImpl(WorkshopRepository workshopRepository) {
        this.workshopRepository = workshopRepository;
    }

    @Override
    public Optional<Workshop> handle(GetWorkshopByUserIdAsyncQuery query) {
        return workshopRepository.findWorkshopByUserId(query.userId());
    }
}
