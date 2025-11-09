package com.safecar.platform.insights.application.internal.outboundservices.analytics;

import com.safecar.platform.insights.domain.model.commands.GenerateVehicleInsightCommand;
import com.safecar.platform.insights.domain.model.valueobjects.TelemetryInsightResult;

public interface TelemetryAnalyticsGateway {

    TelemetryInsightResult analyze(GenerateVehicleInsightCommand command);
}
