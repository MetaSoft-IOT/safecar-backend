package com.safecar.platform.workshopOps.domain.model.queries;

import com.safecar.platform.workshopOps.domain.model.valueobjects.AppointmentStatus;

public record GetAllAppointmentsByStatusQuery(AppointmentStatus status) {
}

