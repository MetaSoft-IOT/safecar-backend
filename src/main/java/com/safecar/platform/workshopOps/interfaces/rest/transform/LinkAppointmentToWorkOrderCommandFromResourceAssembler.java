package com.safecar.platform.workshopOps.interfaces.rest.transform;

import com.safecar.platform.workshopOps.domain.model.commands.LinkAppointmentToWorkOrderCommand;
import com.safecar.platform.workshopOps.domain.model.valueobjects.WorkOrderCode;
import com.safecar.platform.workshopOps.interfaces.rest.resources.LinkAppointmentToWorkOrderResource;

/**
 * Link Appointment To Work Order Command From Resource Assembler - Converts
 * LinkAppointmentToWorkOrderResource to LinkAppointmentToWorkOrderCommand.
 */
public class LinkAppointmentToWorkOrderCommandFromResourceAssembler {

    /**
     * Converts a {@link LinkAppointmentToWorkOrderResource} to a
     * {@link LinkAppointmentToWorkOrderCommand}.
     *
     * @param appointmentId the appointment ID
     * @param resource      the link appointment to work order resource
     * @return the link appointment to work order command
     */
    public static LinkAppointmentToWorkOrderCommand toCommandFromResource(Long appointmentId,
            LinkAppointmentToWorkOrderResource resource) {
        // TODO: For now, we'll use a default workshop ID. In a real implementation,
        // this would be resolved from context
        var workOrderCode = new WorkOrderCode(resource.workOrderCode(), 1L);

        return new LinkAppointmentToWorkOrderCommand(
                appointmentId,
                workOrderCode);
    }
}