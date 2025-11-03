package com.safecar.platform.workshopOps.domain.services;

import java.util.List;
import java.util.Optional;

import com.safecar.platform.workshopOps.domain.model.aggregates.Appointment;
import com.safecar.platform.workshopOps.domain.model.queries.*;

public interface AppointmentQueryService {

    Optional<Appointment> handle(GetAppointmentByIdQuery query);

    Optional<Appointment> handle(GetAppointmentByCodeQuery query);

    List<Appointment> handle(GetAllAppointmentsByCustomerIdQuery query);

    List<Appointment> handle(GetAllAppointmentsByStatusQuery query);

    List<Appointment> handle(GetAllAppointmentsByWorkshopIdQuery query);
}

