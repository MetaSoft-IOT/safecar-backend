package com.safecar.platform.insights.interfaces.rest.transform;

public class RecommendationResourceFromEntityAssembler {

  public static com.safecar.platform.insights.interfaces.rest.resource.RecommendationResource
  toResource(com.safecar.platform.insights.domain.model.aggregates.Recommendation r) {
    return new com.safecar.platform.insights.interfaces.rest.resource.RecommendationResource(
        r.getId(),           // Long
        r.getDriverId(),     // Long
        r.getVehicleId(),    // Long
        r.getTitle(),
        r.getContent(),
        r.getStatus().name()
    );
  }
}
