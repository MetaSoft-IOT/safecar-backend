package com.safecar.platform.workshopOps.interfaces.rest.transform;

import com.safecar.platform.workshopOps.domain.model.commands.RescheduleAppointmentCommand;
import com.safecar.platform.workshopOps.domain.model.valueobjects.ServiceSlot;
import com.safecar.platform.workshopOps.interfaces.rest.resources.RescheduleAppointmentResource;

/**
 * Reschedule Appointment Command From Resource Assembler - Converts
 * RescheduleAppointmentResource to RescheduleAppointmentCommand.
 */
public class RescheduleAppointmentCommandFromResourceAssembler {

    /**
     * Converts a {@link RescheduleAppointmentResource} to a
     * {@link RescheduleAppointmentCommand}.
     *
     * @param appointmentId the appointment ID
     * @param resource      the reschedule appointment resource
     * @return the reschedule appointment command
     */
    public static RescheduleAppointmentCommand toCommandFromResource(Long appointmentId,
            RescheduleAppointmentResource resource) {
        var slot = new ServiceSlot(resource.startAt(), resource.endAt());
        return new RescheduleAppointmentCommand(appointmentId, slot);
    }
}
