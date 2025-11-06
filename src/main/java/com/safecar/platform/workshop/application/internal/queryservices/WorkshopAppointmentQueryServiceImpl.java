package com.safecar.platform.workshop.application.internal.queryservices;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.aggregates.WorkshopAppointment;
import com.safecar.platform.workshop.domain.model.queries.*;
import com.safecar.platform.workshop.domain.services.WorkshopAppointmentQueryService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.WorkshopAppointmentRepository;

/**
 * Workshop Appointment Query Service Implementation 
 * @see WorkshopAppointmentQueryService WorkshopAppointmentQueryService for method details.
 */
@Service
public class WorkshopAppointmentQueryServiceImpl implements WorkshopAppointmentQueryService {

    /**
     * Repository for WorkshopAppointment to handle persistence operations.
     */
    private final WorkshopAppointmentRepository repository;

    public WorkshopAppointmentQueryServiceImpl(WorkshopAppointmentRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<WorkshopAppointment> handle(GetAppointmentByIdQuery query) {
        return repository.findById(query.appointmentId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorkshopAppointment> handle(GetAppointmentsByWorkshopAndRangeQuery query) {
        return repository.findByWorkOrderWorkshopAndScheduledAtStartAtBetween(
            query.workshopId(), 
            query.from(), 
            query.to()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorkshopAppointment> handle(GetAppointmentsByWorkOrderQuery query) {
        return repository.findByWorkOrder_Id(query.workOrderId());
    }
}