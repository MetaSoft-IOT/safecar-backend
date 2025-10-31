package com.safecar.platform.insights.application.internal.commandservices;

import com.safecar.platform.insights.domain.model.aggregates.SafetyScore;
import com.safecar.platform.insights.domain.model.commands.ComputeSafetyScoreCommand;
import com.safecar.platform.insights.domain.model.valueobjects.ScoreValue;
import com.safecar.platform.insights.domain.services.SafetyScoreCommandService;
import com.safecar.platform.insights.infrastructure.persistence.jpa.repositories.SafetyScoreRepository;
import com.safecar.platform.insights.application.internal.outboundservices.acl.InsightsLLMClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SafetyScoreCommandServiceImpl implements SafetyScoreCommandService {

  private final SafetyScoreRepository repo;
  private final InsightsLLMClient llm;

  public SafetyScoreCommandServiceImpl(SafetyScoreRepository repo, InsightsLLMClient llm) {
    this.repo = repo; this.llm = llm;
  }

  @Override
  public Optional<com.safecar.platform.insights.domain.model.aggregates.SafetyScore>
  handle(ComputeSafetyScoreCommand cmd) {
    var result = llm.scoreFromTelemetry(cmd.telemetryWindowJson());
    var score = new com.safecar.platform.insights.domain.model.aggregates.SafetyScore(
        cmd.driverId(), cmd.vehicleId(),
        new com.safecar.platform.insights.domain.model.valueobjects.ScoreValue(result.value()),
        result.explanation()
    );
    return Optional.of(repo.save(score));
  }
}
