package com.safecar.platform.insights.domain.model.commands;
public record GenerateRecommendationsCommand(Long driverId, Long vehicleId, String summaryJson) {}
