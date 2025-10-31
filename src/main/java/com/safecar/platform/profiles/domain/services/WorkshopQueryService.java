package com.safecar.platform.profiles.domain.services;



import com.safecar.platform.profiles.domain.model.aggregates.Workshop;
import com.safecar.platform.profiles.domain.model.queries.GetWorkshopByUserIdAsyncQuery;

import java.util.Optional;

public interface WorkshopQueryService {
    Optional<Workshop> handle(GetWorkshopByUserIdAsyncQuery query);
}
