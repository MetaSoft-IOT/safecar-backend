package com.safecar.platform.profiles.interfaces.rest.transform;

import com.safecar.platform.profiles.domain.model.aggregates.Driver;
import com.safecar.platform.profiles.interfaces.rest.resource.DriverResource;

/*
 * Driver Resource From Entity Assembler
 * <p>
 * Transforms a Driver domain entity into a DriverResource for REST API responses.
 * </p>
 */
public class DriverResourceFromEntityAssembler {
    /**
     * Transforms a Driver domain entity into a DriverResource.
     * 
     * @param driver The Driver entity to transform.
     * @return The corresponding DriverResource.
     */
    public static DriverResource toResourceFromEntity(Driver driver) {
        return new DriverResource(
                driver.getUserId(), driver.getId(), driver.getFullName(),
                driver.getCity(), driver.getCountry(), driver.getPhone(),
                driver.getDni());
    }
}
