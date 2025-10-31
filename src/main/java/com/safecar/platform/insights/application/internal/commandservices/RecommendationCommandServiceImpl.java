package com.safecar.platform.insights.application.internal.commandservices;

import com.safecar.platform.insights.domain.model.aggregates.Recommendation;
import com.safecar.platform.insights.domain.model.commands.GenerateRecommendationsCommand;
import com.safecar.platform.insights.domain.services.RecommendationCommandService;
import com.safecar.platform.insights.infrastructure.persistence.jpa.repositories.RecommendationRepository;
import com.safecar.platform.insights.application.internal.outboundservices.acl.InsightsLLMClient;
import com.safecar.platform.insights.application.internal.outboundservices.acl.AlertsContextFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RecommendationCommandServiceImpl implements RecommendationCommandService {

  private final RecommendationRepository repo;
  private final InsightsLLMClient llm;
  private final AlertsContextFacade alerts; // puede ser un stub mientras integras

  public RecommendationCommandServiceImpl(RecommendationRepository repo, InsightsLLMClient llm, AlertsContextFacade alerts) {
    this.repo = repo; this.llm = llm; this.alerts = alerts;
  }

  public List<Recommendation> handle(GenerateRecommendationsCommand cmd, boolean sendToAlerts) {
    String text = llm.generateRecommendations(cmd.summaryJson());
    List<Recommendation> out = new java.util.ArrayList<>();
    for (String line : text.split("\\r?\\n")) {
      String l = line.trim();
      if (l.startsWith("-") || l.startsWith("*")) {
        String content = l.substring(1).trim();
        var rec = new Recommendation(cmd.driverId(), cmd.vehicleId(), "Coaching", content);
        rec.publish();
        out.add(repo.save(rec));
        if (sendToAlerts) {
      alerts.sendCoaching(
        cmd.driverId(),         // Long
        cmd.vehicleId(),        // Long
        rec.getTitle(),
        rec.getContent(),
        2
      );
      rec.markSent();
    }
      }
    }
    return out;
  }
}
