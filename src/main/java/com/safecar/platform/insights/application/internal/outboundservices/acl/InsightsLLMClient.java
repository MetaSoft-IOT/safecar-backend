package com.safecar.platform.insights.application.internal.outboundservices.acl;

public interface InsightsLLMClient {
  /** Devuelve un score 0..100 y una explicación corta en texto. */
  LLMScoreResult scoreFromTelemetry(String telemetryWindowJson);

  /** Devuelve un bloque de recomendaciones en markdown o texto plano. */
  String generateRecommendations(String summaryJson);

  record LLMScoreResult(int value, String explanation) {}
}
