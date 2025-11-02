package com.safecar.platform.appointments.domain.model.queries;

import java.util.UUID;

public record GetAllAppointmentsByCustomerIdQuery(UUID customerId) {
}

