package com.safecar.platform.insights.interfaces.rest;

import com.safecar.platform.insights.domain.model.commands.ComputeSafetyScoreCommand;
import com.safecar.platform.insights.domain.model.commands.GenerateRecommendationsCommand;
import com.safecar.platform.insights.domain.model.queries.GetLatestSafetyScoreQuery;
import com.safecar.platform.insights.domain.model.queries.GetRecommendationsQuery;
import com.safecar.platform.insights.domain.services.*;
import com.safecar.platform.insights.interfaces.rest.resource.*;
import com.safecar.platform.insights.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/v1/insights", produces=MediaType.APPLICATION_JSON_VALUE)
@Tag(name="Insights", description="Analytics & Recommendations Endpoints")
public class InsightsController {

  private final SafetyScoreCommandService scoreCmd;
  private final SafetyScoreQueryService scoreQry;
  private final RecommendationCommandService recoCmd;
  private final RecommendationQueryService recoQry;

  public InsightsController(SafetyScoreCommandService scoreCmd, SafetyScoreQueryService scoreQry,
                            RecommendationCommandService recoCmd, RecommendationQueryService recoQry) {
    this.scoreCmd = scoreCmd; this.scoreQry = scoreQry; this.recoCmd = recoCmd; this.recoQry = recoQry;
  }

  // Compute score (usa OpenAI debajo)
  @PostMapping("/score/{driverId}/{vehicleId}")
  public ResponseEntity<SafetyScoreResource> computeScore(@PathVariable Long driverId,
                                                        @PathVariable Long vehicleId,
                                                        @RequestBody String telemetryWindowJson) {
    var cmd = new ComputeSafetyScoreCommand(driverId, vehicleId, telemetryWindowJson);
    var saved = scoreCmd.handle(cmd);
    return saved.map(e -> ResponseEntity.ok(SafetyScoreResourceFromEntityAssembler.toResource(e)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build());
  }

  // Get latest score
  @GetMapping("/score/{driverId}/{vehicleId}")
    public ResponseEntity<SafetyScoreResource> getLatest(@PathVariable Long driverId,
                                                     @PathVariable Long vehicleId) {
    var q = new GetLatestSafetyScoreQuery(driverId, vehicleId);
    var e = scoreQry.handle(q);
    return e.map(x -> ResponseEntity.ok(SafetyScoreResourceFromEntityAssembler.toResource(x)))
            .orElse(ResponseEntity.notFound().build());
  }

  // Generate recommendations (y opcionalmente las envía a Alerts)
  @PostMapping("/recommendations/{driverId}/{vehicleId}")
  public ResponseEntity<List<RecommendationResource>> generate(@PathVariable Long driverId,
                                                             @PathVariable Long vehicleId,
                                                             @RequestParam(defaultValue="false") boolean send,
                                                             @RequestBody String summaryJson) {
    var cmd = new GenerateRecommendationsCommand(driverId, vehicleId, summaryJson);
    var list = recoCmd.handle(cmd, send).stream().map(RecommendationResourceFromEntityAssembler::toResource).toList();
    return ResponseEntity.ok(list);
  }

  // Query recommendations
  @GetMapping("/recommendations/{driverId}/{vehicleId}")
  public List<RecommendationResource> list(@PathVariable Long driverId,
                                         @PathVariable Long vehicleId) {
    return recoQry.handle(new GetRecommendationsQuery(driverId, vehicleId))
                  .stream().map(RecommendationResourceFromEntityAssembler::toResource).toList();
  }
}
