package com.safecar.platform.workshopOps.interfaces.rest.transform;

import com.safecar.platform.workshopOps.domain.model.aggregates.Appointment;
import com.safecar.platform.workshopOps.interfaces.rest.resources.AppointmentResource;

import java.util.stream.Collectors;

/**
 * Assembler class for converting {@link Appointment} entities into {@link AppointmentResource} objects.
 * <p>
 * Used to transform domain appointment aggregates into resources suitable for API responses.
 * </p>
 */
public class AppointmentResourceFromEntityAssembler {

    /**
     * Converts an {@link Appointment} entity into an {@link AppointmentResource}.
     *
     * @param entity the appointment aggregate entity to convert
     * @return the corresponding {@link AppointmentResource}
     */
    public static AppointmentResource toResourceFromEntity(Appointment entity) {
        return new AppointmentResource(
                entity.getId(),
                entity.getCode(),
                entity.getScheduledDate(),
                entity.getEndDate(),
                entity.getStatus().name(),
                entity.getServiceType(),
                entity.getDescription(),
                entity.getCustomerId(),
                entity.getVehicleId(),
                entity.getMechanicId(),
                entity.getWorkshopId(),
                entity.getNotes().stream()
                        .map(AppointmentNoteResourceFromEntityAssembler::toResourceFromEntity)
                        .collect(Collectors.toList())
        );
    }
}

