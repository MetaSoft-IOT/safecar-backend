package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.commands.LinkAppointmentToServiceOrderCommand;
import com.safecar.platform.workshop.domain.model.valueobjects.ServiceOrderCode;
import com.safecar.platform.workshop.interfaces.rest.resources.LinkAppointmentToServiceOrderResource;

/**
 * Link Appointment To Service Order Command From Resource Assembler - Converts
 * LinkAppointmentToServiceOrderResource to
 * LinkAppointmentToServiceOrderCommand.
 */
public class LinkAppointmentToServiceOrderCommandFromResourceAssembler {

    /**
     * Converts a {@link LinkAppointmentToServiceOrderResource} to a
     * {@link LinkAppointmentToServiceOrderCommand}.
     *
     * @param appointmentId the appointment ID
     * @param resource      the link appointment to service order resource
     * @return the link appointment to service order command
     */
    public static LinkAppointmentToServiceOrderCommand toCommandFromResource(Long appointmentId,
            LinkAppointmentToServiceOrderResource resource) {

        var serviceOrderCode = resource.serviceOrderCode();
        var codeValue = resource.serviceOrderCode();

        if (serviceOrderCode == null || !serviceOrderCode.startsWith("SO-"))
            throw new IllegalArgumentException(
                    "Invalid service order code format. Expected format: SO-{workshopId}-{timestamp}");

        var parts = serviceOrderCode.split("-");

        if (parts.length < 3)
            throw new IllegalArgumentException(
                    "Invalid service order code format. Expected format: SO-{workshopId}-{timestamp}");

        var workshopId = Long.parseLong(parts[1]);

        return new LinkAppointmentToServiceOrderCommand(
                appointmentId,
                new ServiceOrderCode(codeValue, workshopId));
    }
}