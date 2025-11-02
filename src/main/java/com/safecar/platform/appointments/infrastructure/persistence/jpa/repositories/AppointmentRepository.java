package com.safecar.platform.appointments.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.appointments.domain.model.aggregates.Appointment;
import com.safecar.platform.appointments.domain.model.valueobjects.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    Optional<Appointment> findByCode(String code);

    List<Appointment> findByCustomerId(UUID customerId);

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByWorkshopId(UUID workshopId);

    List<Appointment> findByMechanicId(UUID mechanicId);

    List<Appointment> findByVehicleId(UUID vehicleId);

    boolean existsByCode(String code);
}

