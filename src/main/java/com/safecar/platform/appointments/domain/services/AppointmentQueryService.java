package com.safecar.platform.appointments.domain.services;

import com.safecar.platform.appointments.domain.model.aggregates.Appointment;
import com.safecar.platform.appointments.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface AppointmentQueryService {

    Optional<Appointment> handle(GetAppointmentByIdQuery query);

    Optional<Appointment> handle(GetAppointmentByCodeQuery query);

    List<Appointment> handle(GetAllAppointmentsByCustomerIdQuery query);

    List<Appointment> handle(GetAllAppointmentsByStatusQuery query);

    List<Appointment> handle(GetAllAppointmentsByWorkshopIdQuery query);
}

