package com.safecar.platform.insights.interfaces.rest.resource;

import java.util.UUID;

public record RecommendationResource(
    UUID id,
    Long driverId,
    Long vehicleId,
    String title,
    String content,
    String status
) {}
