package com.safecar.platform.profiles.domain.services;

import com.safecar.platform.profiles.domain.model.aggregates.Driver;
import com.safecar.platform.profiles.domain.model.queries.GetDriverByUserIdQuery;

import java.util.Optional;

/**
 * Driver Query Service Interface
 * <p>
 * This interface defines the contract for querying driver information based on
 * user ID.
 * It provides a method to handle the retrieval of driver details encapsulated
 * in a query object.
 * </p>
 */
public interface DriverQueryService {
    /**
     * Handles the retrieval of a Driver by user ID.
     * 
     * @param query The query object containing the user ID.
     * @return An Optional containing the Driver if found, otherwise empty.
     */
    Optional<Driver> handle(GetDriverByUserIdQuery query);
}
