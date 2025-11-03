package com.safecar.platform.workshopOps.domain.services;

import java.util.Optional;

import com.safecar.platform.workshopOps.domain.model.aggregates.Appointment;
import com.safecar.platform.workshopOps.domain.model.commands.*;

public interface AppointmentCommandService {

    Optional<Appointment> handle(CreateAppointmentCommand command);

    Optional<Appointment> handle(ConfirmAppointmentCommand command);

    Optional<Appointment> handle(CancelAppointmentCommand command);

    Optional<Appointment> handle(RescheduleAppointmentCommand command);

    Optional<Appointment> handle(AssignMechanicCommand command);

    Optional<Appointment> handle(StartAppointmentCommand command);

    Optional<Appointment> handle(CompleteAppointmentCommand command);

    Optional<Appointment> handle(UpdateAppointmentInformationCommand command);

    void handle(AddAppointmentNoteCommand command);
}

