package com.safecar.platform.insights.application.internal.commandservices;

import com.safecar.platform.insights.application.internal.outboundservices.analytics.TelemetryAnalyticsGateway;
import com.safecar.platform.insights.domain.model.aggregates.VehicleInsight;
import com.safecar.platform.insights.domain.model.commands.GenerateVehicleInsightCommand;
import com.safecar.platform.insights.domain.services.VehicleInsightCommandService;
import com.safecar.platform.insights.infrastructure.persistence.jpa.repositories.VehicleInsightRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleInsightCommandServiceImpl implements VehicleInsightCommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleInsightCommandServiceImpl.class);

    private final VehicleInsightRepository repository;
    private final TelemetryAnalyticsGateway analyticsGateway;

    @Override
    @Transactional
    public VehicleInsight handle(GenerateVehicleInsightCommand command) {
        var analysis = analyticsGateway.analyze(command);
        LOGGER.debug("Telemetry analytics completed for vehicle {} with risk {}", 
                command.vehicle().vehicleId(), analysis.riskLevel());
        var insight = new VehicleInsight(command.vehicle(), analysis);
        return repository.save(insight);
    }
}
