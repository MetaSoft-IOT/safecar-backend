package com.safecar.platform.insights.interfaces.rest.transform;

public class SafetyScoreResourceFromEntityAssembler {

  public static com.safecar.platform.insights.interfaces.rest.resource.SafetyScoreResource
  toResource(com.safecar.platform.insights.domain.model.aggregates.SafetyScore e) {
    return new com.safecar.platform.insights.interfaces.rest.resource.SafetyScoreResource(
        e.getId(),            // Long
        e.getDriverId(),      // Long
        e.getVehicleId(),     // Long
        e.getValue().value(),
        e.getExplanation()
    );
  }
}
