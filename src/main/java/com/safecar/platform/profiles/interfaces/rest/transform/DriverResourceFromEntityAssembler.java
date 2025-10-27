package com.safecar.platform.profiles.interfaces.rest.transform;


import com.safecar.platform.profiles.domain.model.aggregates.Driver;
import com.safecar.platform.profiles.interfaces.rest.resource.DriverResource;

public class DriverResourceFromEntityAssembler {
    public static DriverResource toResourceFromEntity(Driver driver) {
        return new DriverResource(
                driver.getUserId(), driver.getId(), driver.getFullName(),
                driver.getCity(), driver.getCountry(), driver.getPhone(),
                driver.getDni()
        );
    }
}
