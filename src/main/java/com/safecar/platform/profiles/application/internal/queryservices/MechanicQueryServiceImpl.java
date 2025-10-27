package com.safecar.platform.profiles.application.internal.queryservices;

import com.safecar.platform.profiles.domain.model.aggregates.Mechanic;
import com.safecar.platform.profiles.domain.model.queries.GetMechanicByUserIdAsyncQuery;
import com.safecar.platform.profiles.domain.services.MechanicQueryService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.MechanicRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MechanicQueryServiceImpl implements MechanicQueryService {
    private final MechanicRepository mechanicRepository;

    public MechanicQueryServiceImpl(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }

    @Override
    public Optional<Mechanic> handle(GetMechanicByUserIdAsyncQuery query) {
        return mechanicRepository.findMechanicByUserId(query.userId());
    }
}
