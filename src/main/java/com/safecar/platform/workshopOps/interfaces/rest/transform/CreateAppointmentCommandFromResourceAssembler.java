package com.safecar.platform.workshopOps.interfaces.rest.transform;

import com.safecar.platform.workshopOps.domain.model.commands.CreateAppointmentCommand;
import com.safecar.platform.workshopOps.domain.model.valueobjects.*;
import com.safecar.platform.workshopOps.interfaces.rest.resources.CreateAppointmentResource;

/**
 * Create Appointment Command From Resource Assembler - Converts
 * CreateAppointmentResource to CreateAppointmentCommand.
 */
public class CreateAppointmentCommandFromResourceAssembler {

    /**
     * Converts a {@link CreateAppointmentResource} to a
     * {@link CreateAppointmentCommand}.
     *
     * @param resource the create appointment resource
     * @return the create appointment command
     */
    public static CreateAppointmentCommand toCommandFromResource(CreateAppointmentResource resource) {
        var workshopId = new WorkshopId(resource.workshopId(), "Workshop " + resource.workshopId());
        var vehicleId = new VehicleId(resource.vehicleId(), "PLATE-" + resource.vehicleId());
        var driverId = new DriverId(resource.driverId(), "Driver " + resource.driverId());
        var slot = new ServiceSlot(resource.startAt(), resource.endAt());

        return new CreateAppointmentCommand(
                workshopId,
                vehicleId,
                driverId,
                slot);
    }
}
