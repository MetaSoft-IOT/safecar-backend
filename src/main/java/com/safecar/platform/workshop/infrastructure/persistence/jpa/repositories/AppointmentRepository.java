package com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safecar.platform.workshop.domain.model.aggregates.Appointment;
import com.safecar.platform.workshop.domain.model.valueobjects.AppointmentStatus;
import com.safecar.platform.workshop.domain.model.valueobjects.DriverId;
import com.safecar.platform.workshop.domain.model.valueobjects.VehicleId;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

import java.time.Instant;
import java.util.List;

/**
 * Workshop Appointment Repository - JPA Implementation to manage WorkshopAppointment entities.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

   /**
    * Finds all WorkshopAppointments for a given WorkshopId.
    * @param workshopId the ID of the workshop
    * @return list of WorkshopAppointments associated with the specified workshop
    */
    List<Appointment> findByServiceOrderWorkshop(WorkshopId workshopId);
    /**
     * Finds all WorkshopAppointments for a given VehicleId.
     * @param vehicleId the ID of the vehicle
     * @return list of WorkshopAppointments associated with the specified vehicle
     */
    List<Appointment> findByServiceOrderVehicle(VehicleId vehicleId);
    /*
     * Finds all WorkshopAppointments for a given DriverId.
     * @param driverId the ID of the driver
     * @return list of WorkshopAppointments associated with the specified driver
     */
    List<Appointment> findByServiceOrderDriver(DriverId driverId);
    /**
     * Finds all WorkshopAppointments linked to a specific Service Order ID.
     * @param serviceOrderId the ID of the linked service order
     * @return list of WorkshopAppointments associated with the specified service order
     */
    List<Appointment> findByServiceOrder_Id(Long serviceOrderId);
    /**
     * Finds all WorkshopAppointments with a specific status.
     * @param status the status of the appointment
     * @return list of WorkshopAppointments with the specified status
     */
    List<Appointment> findByStatus(AppointmentStatus status);
    /**
     * Finds all WorkshopAppointments scheduled between the specified start and end dates.
     * @param startDate the start date of the appointment
     * @param endDate the end date of the appointment
     * @return list of WorkshopAppointments scheduled between the specified dates
     */
    List<Appointment> findByScheduledAtStartAtBetween(Instant startDate, Instant endDate);
    /**
     * Finds all WorkshopAppointments for a specific WorkshopId scheduled between the specified start and end dates.
     * @param workshopId the ID of the workshop
     * @param startDate the start date of the appointment
     * @param endDate the end date of the appointment
     * @return list of WorkshopAppointments for the specified workshop scheduled between the specified dates
     */
    List<Appointment> findByServiceOrderWorkshopAndScheduledAtStartAtBetween(WorkshopId workshopId, Instant startDate, Instant endDate);
}