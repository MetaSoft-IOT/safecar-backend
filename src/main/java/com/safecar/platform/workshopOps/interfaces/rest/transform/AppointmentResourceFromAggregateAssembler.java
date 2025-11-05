package com.safecar.platform.workshopOps.interfaces.rest.transform;

import com.safecar.platform.workshopOps.domain.model.aggregates.WorkshopAppointment;
import com.safecar.platform.workshopOps.interfaces.rest.resources.AppointmentResource;

import java.util.stream.Collectors;

/**
 * Appointment Resource Assembler from WorkshopAppointment Aggregate - Converts
 * WorkshopAppointment aggregates to AppointmentResource representations.
 */
public class AppointmentResourceFromAggregateAssembler {

    /**
     * Converts a {@link WorkshopAppointment} aggregate to an
     * {@link AppointmentResource}.
     *
     * @param aggregate the workshop appointment aggregate
     * @return the appointment resource
     */
    public static AppointmentResource toResourceFromAggregate(WorkshopAppointment aggregate) {
        var notes = aggregate.getNotes().stream()
                .map(AppointmentNoteResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return new AppointmentResource(
                aggregate.getId(),
                aggregate.getWorkshop().workshopId(),
                aggregate.getVehicle().vehicleId(),
                aggregate.getDriver().driverId(),
                aggregate.getLinkedWorkOrderId(),
                aggregate.getScheduledAt().startAt(),
                aggregate.getScheduledAt().endAt(),
                aggregate.getStatus().name(),
                notes);
    }
}