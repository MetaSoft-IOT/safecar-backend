package com.safecar.platform.profiles.domain.services;



import com.safecar.platform.profiles.domain.model.aggregates.Mechanic;
import com.safecar.platform.profiles.domain.model.queries.GetMechanicByUserIdAsyncQuery;

import java.util.Optional;

public interface MechanicQueryService {
    Optional<Mechanic> handle(GetMechanicByUserIdAsyncQuery query);
}
