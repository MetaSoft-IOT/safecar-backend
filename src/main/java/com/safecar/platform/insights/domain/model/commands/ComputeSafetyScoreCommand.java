package com.safecar.platform.insights.domain.model.commands;
public record ComputeSafetyScoreCommand(Long driverId, Long vehicleId, String telemetryWindowJson) {}
