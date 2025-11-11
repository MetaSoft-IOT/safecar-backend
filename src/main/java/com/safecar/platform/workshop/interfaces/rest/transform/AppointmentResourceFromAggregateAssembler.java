package com.safecar.platform.workshop.interfaces.rest.transform;

import java.util.stream.Collectors;

import com.safecar.platform.workshop.domain.model.aggregates.Appointment;
import com.safecar.platform.workshop.interfaces.rest.resources.AppointmentResource;

/**
 * Appointment Resource Assembler from WorkshopAppointment Aggregate - Converts
 * WorkshopAppointment aggregates to AppointmentResource representations.
 */
public class AppointmentResourceFromAggregateAssembler {

    /**
     * Converts a {@link Appointment} aggregate to an
     * {@link AppointmentResource}.
     *
     * @param aggregate the workshop appointment aggregate
     * @return the appointment resource
     */
    public static AppointmentResource toResourceFromAggregate(Appointment aggregate) {
        var notes = aggregate.getNotes().stream()
                .map(AppointmentNoteResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return new AppointmentResource(
                aggregate.getId(),
                aggregate.getWorkshop().workshopId(),
                aggregate.getVehicle().vehicleId(),
                aggregate.getDriver().driverId(),
                aggregate.getServiceOrderId(),
                aggregate.getScheduledAt().startAt(),
                aggregate.getScheduledAt().endAt(),
                aggregate.getStatus().name(),
                notes);
    }
}