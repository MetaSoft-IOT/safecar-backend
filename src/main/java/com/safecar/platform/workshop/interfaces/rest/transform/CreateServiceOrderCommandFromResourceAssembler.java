package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.commands.OpenServiceOrderCommand;
import com.safecar.platform.workshop.domain.model.valueobjects.*;
import com.safecar.platform.workshop.interfaces.rest.resources.CreateServiceOrderResource;

import java.time.Instant;

/**
 * Create Service Order Command From Resource Assembler - Converts
 * CreateServiceOrderResource to OpenServiceOrderCommand.
 * <p>
 * This assembler handles the transformation from REST layer to domain layer,
 * ensuring proper value object creation for SafeCar's workshop operations.
 * </p>
 */
public class CreateServiceOrderCommandFromResourceAssembler {

    /**
     * Converts a {@link CreateServiceOrderResource} to an {@link OpenServiceOrderCommand}.
     *
     * @param resource the create service order resource
     * @return the open service order command
     */
    public static OpenServiceOrderCommand toCommandFromResource(CreateServiceOrderResource resource) {
        // Generate unique service order code with timestamp
        var codeValue = "SO-" + System.currentTimeMillis();
        var serviceOrderCode = new ServiceOrderCode(codeValue, resource.workshopId());
        
        return new OpenServiceOrderCommand(
                new WorkshopId(resource.workshopId()),
                new VehicleId(resource.vehicleId()),
                new DriverId(resource.driverId()),
                serviceOrderCode,
                Instant.now()
        );
    }
}