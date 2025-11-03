package com.safecar.platform.workshopOps.application.internal.queryservices;

import com.safecar.platform.workshopOps.domain.model.aggregates.Appointment;
import com.safecar.platform.workshopOps.domain.model.queries.*;
import com.safecar.platform.workshopOps.domain.services.AppointmentQueryService;
import com.safecar.platform.workshopOps.infrastructure.persistence.jpa.repositories.AppointmentRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentQueryServiceImpl implements AppointmentQueryService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentQueryServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Optional<Appointment> handle(GetAppointmentByIdQuery query) {
        return appointmentRepository.findById(query.appointmentId());
    }

    @Override
    public Optional<Appointment> handle(GetAppointmentByCodeQuery query) {
        return appointmentRepository.findByCode(query.code());
    }

    @Override
    public List<Appointment> handle(GetAllAppointmentsByCustomerIdQuery query) {
        return appointmentRepository.findByCustomerId(query.customerId());
    }

    @Override
    public List<Appointment> handle(GetAllAppointmentsByStatusQuery query) {
        return appointmentRepository.findByStatus(query.status());
    }

    @Override
    public List<Appointment> handle(GetAllAppointmentsByWorkshopIdQuery query) {
        return appointmentRepository.findByWorkshopId(query.workshopId());
    }
}

