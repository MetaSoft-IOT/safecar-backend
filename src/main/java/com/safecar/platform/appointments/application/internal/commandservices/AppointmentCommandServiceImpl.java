package com.safecar.platform.appointments.application.internal.commandservices;

import com.safecar.platform.appointments.domain.model.aggregates.Appointment;
import com.safecar.platform.appointments.domain.model.commands.*;
import com.safecar.platform.appointments.domain.services.AppointmentCommandService;
import com.safecar.platform.appointments.infrastructure.persistence.jpa.repositories.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AppointmentCommandServiceImpl implements AppointmentCommandService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentCommandServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    @Transactional
    public Optional<Appointment> handle(CreateAppointmentCommand command) {
        if (appointmentRepository.existsByCode(command.code())) {
            throw new IllegalArgumentException("Ya existe una cita con el código: " + command.code());
        }
        var appointment = new Appointment(command);
        var createdAppointment = appointmentRepository.save(appointment);
        return Optional.of(createdAppointment);
    }

    @Override
    @Transactional
    public Optional<Appointment> handle(ConfirmAppointmentCommand command) {
        var appointment = appointmentRepository.findById(command.appointmentId());
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("Cita no encontrada con id: " + command.appointmentId());
        }
        var confirmedAppointment = appointment.get().confirm();
        appointmentRepository.save(confirmedAppointment);
        return Optional.of(confirmedAppointment);
    }

    @Override
    @Transactional
    public Optional<Appointment> handle(CancelAppointmentCommand command) {
        var appointment = appointmentRepository.findById(command.appointmentId());
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("Cita no encontrada con id: " + command.appointmentId());
        }
        var cancelledAppointment = appointment.get().cancel(command.reason());
        appointmentRepository.save(cancelledAppointment);
        return Optional.of(cancelledAppointment);
    }

    @Override
    @Transactional
    public Optional<Appointment> handle(RescheduleAppointmentCommand command) {
        var appointment = appointmentRepository.findById(command.appointmentId());
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("Cita no encontrada con id: " + command.appointmentId());
        }
        var rescheduledAppointment = appointment.get().reschedule(command.newScheduledDate());
        appointmentRepository.save(rescheduledAppointment);
        return Optional.of(rescheduledAppointment);
    }

    @Override
    @Transactional
    public Optional<Appointment> handle(AssignMechanicCommand command) {
        var appointment = appointmentRepository.findById(command.appointmentId());
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("Cita no encontrada con id: " + command.appointmentId());
        }
        var updatedAppointment = appointment.get().assignMechanic(command.mechanicId());
        appointmentRepository.save(updatedAppointment);
        return Optional.of(updatedAppointment);
    }

    @Override
    @Transactional
    public Optional<Appointment> handle(StartAppointmentCommand command) {
        var appointment = appointmentRepository.findById(command.appointmentId());
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("Cita no encontrada con id: " + command.appointmentId());
        }
        var startedAppointment = appointment.get().start();
        appointmentRepository.save(startedAppointment);
        return Optional.of(startedAppointment);
    }

    @Override
    @Transactional
    public Optional<Appointment> handle(CompleteAppointmentCommand command) {
        var appointment = appointmentRepository.findById(command.appointmentId());
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("Cita no encontrada con id: " + command.appointmentId());
        }
        var completedAppointment = appointment.get().complete();
        appointmentRepository.save(completedAppointment);
        return Optional.of(completedAppointment);
    }

    @Override
    @Transactional
    public Optional<Appointment> handle(UpdateAppointmentInformationCommand command) {
        var appointment = appointmentRepository.findById(command.appointmentId());
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("Cita no encontrada con id: " + command.appointmentId());
        }
        var updatedAppointment = appointment.get().updateInformation(
                command.serviceType(),
                command.description()
        );
        appointmentRepository.save(updatedAppointment);
        return Optional.of(updatedAppointment);
    }

    @Override
    @Transactional
    public void handle(AddAppointmentNoteCommand command) {
        var appointment = appointmentRepository.findById(command.appointmentId());
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("Cita no encontrada con id: " + command.appointmentId());
        }
        appointment.get().addNote(command.content(), command.authorId());
        appointmentRepository.save(appointment.get());
    }
}

