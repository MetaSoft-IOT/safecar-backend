package com.safecar.platform.profiles.domain.services;


import com.safecar.platform.profiles.domain.model.aggregates.Driver;
import com.safecar.platform.profiles.domain.model.queries.GetDriverByUserIdAsyncQuery;

import java.util.Optional;

public interface DriverQueryService {
    Optional<Driver> handle(GetDriverByUserIdAsyncQuery query);
}
