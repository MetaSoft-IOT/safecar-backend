package com.safecar.platform.workshop.interfaces.rest.resources;

public record WorkshopOperationResource(
        Long id,
        Long workshopId,
        Integer totalAppointments,
        Integer totalServiceOrders,
        Integer activeOperations,
        Double efficiencyRatio) {
}
