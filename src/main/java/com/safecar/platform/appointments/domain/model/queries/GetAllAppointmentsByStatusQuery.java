package com.safecar.platform.appointments.domain.model.queries;

import com.safecar.platform.appointments.domain.model.valueobjects.AppointmentStatus;

public record GetAllAppointmentsByStatusQuery(AppointmentStatus status) {
}

