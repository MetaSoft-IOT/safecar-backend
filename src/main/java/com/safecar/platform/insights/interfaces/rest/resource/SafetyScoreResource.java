package com.safecar.platform.insights.interfaces.rest.resource;

import java.util.UUID;

public record SafetyScoreResource(
    UUID scoreId,
    Long driverId,
    Long vehicleId,
    int value,
    String explanation
) {}
