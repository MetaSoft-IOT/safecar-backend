package com.safecar.platform.profiles.application.internal.queryservices;

import com.safecar.platform.profiles.domain.model.aggregates.Driver;
import com.safecar.platform.profiles.domain.model.queries.GetDriverByUserIdAsyncQuery;
import com.safecar.platform.profiles.domain.services.DriverQueryService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DriverQueryServiceImpl implements DriverQueryService {
    private final DriverRepository driverRepository;

    public DriverQueryServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Optional<Driver> handle(GetDriverByUserIdAsyncQuery query) {
        return driverRepository.findDriverByUserId(query.userId());
    }
}
