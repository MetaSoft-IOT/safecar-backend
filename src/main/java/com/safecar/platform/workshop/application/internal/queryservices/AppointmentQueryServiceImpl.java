package com.safecar.platform.workshop.application.internal.queryservices;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.aggregates.Appointment;
import com.safecar.platform.workshop.domain.model.queries.*;
import com.safecar.platform.workshop.domain.services.AppointmentQueryService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.AppointmentRepository;

/**
 * Workshop Appointment Query Service Implementation 
 * @see AppointmentQueryService WorkshopAppointmentQueryService for method details.
 */
@Service
public class AppointmentQueryServiceImpl implements AppointmentQueryService {

    /**
     * Repository for WorkshopAppointment to handle persistence operations.
     */
    private final AppointmentRepository repository;

    public AppointmentQueryServiceImpl(AppointmentRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Appointment> handle(GetAppointmentByIdQuery query) {
        return repository.findById(query.appointmentId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Appointment> handle(GetAppointmentsByWorkshopAndRangeQuery query) {
        return repository.findByServiceOrderWorkshopAndScheduledAtStartAtBetween(
            query.workshopId(), 
            query.from(), 
            query.to()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Appointment> handle(GetAppointmentsByServiceOrderQuery query) {
        return repository.findByServiceOrder_Id(query.serviceOrderId());
    }
}