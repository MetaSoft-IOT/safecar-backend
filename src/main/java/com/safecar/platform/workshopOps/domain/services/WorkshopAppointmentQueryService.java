package com.safecar.platform.workshopOps.domain.services;

import java.util.List;
import java.util.Optional;

import com.safecar.platform.workshopOps.domain.model.aggregates.WorkshopAppointment;
import com.safecar.platform.workshopOps.domain.model.queries.*;

/**
 * Workshop Appointment Query Service - handles queries related to workshop appointments.
 */
public interface WorkshopAppointmentQueryService {
    /**
     * Handles retrieving an appointment by its ID.
     * @param query the query containing the appointment ID
     * @return an optional WorkshopAppointment
     */
    Optional<WorkshopAppointment> handle(GetAppointmentByIdQuery query);
    /**
     * Handles retrieving appointments by workshop ID and date range.
     * @param query the query containing workshop ID and date range
     * @return  a list of WorkshopAppointments
     */
    List<WorkshopAppointment> handle(GetAppointmentsByWorkshopAndRangeQuery query);
    /**
     * Handles retrieving appointments by work order code.
     * @param query the query containing the work order code
     * @return a list of WorkshopAppointments
     */
    List<WorkshopAppointment> handle(GetAppointmentsByWorkOrderQuery query);
}

