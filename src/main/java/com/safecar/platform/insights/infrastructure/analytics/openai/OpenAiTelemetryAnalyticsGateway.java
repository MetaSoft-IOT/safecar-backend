package com.safecar.platform.insights.infrastructure.analytics.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safecar.platform.insights.application.internal.outboundservices.analytics.TelemetryAnalyticsGateway;
import com.safecar.platform.insights.domain.exceptions.TelemetryAnalysisException;
import com.safecar.platform.insights.domain.model.commands.GenerateVehicleInsightCommand;
import com.safecar.platform.insights.domain.model.valueobjects.InsightRecommendation;
import com.safecar.platform.insights.domain.model.valueobjects.TelemetryInsightResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OpenAiTelemetryAnalyticsGateway implements TelemetryAnalyticsGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAiTelemetryAnalyticsGateway.class);

    @Qualifier("insightsRestTemplate")
    private final RestTemplate insightsRestTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${insights.openai.model:gpt-4o-mini}")
    private String model;

    @Value("${insights.openai.api-url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;

    @jakarta.annotation.PostConstruct
    public void logConfiguration() {
        LOGGER.info("OpenAI Telemetry gateway configured. API URL: {}, model: {}, API key loaded: {}", 
                apiUrl, model, apiKey != null && !apiKey.isBlank());
    }

    @Override
    public TelemetryInsightResult analyze(GenerateVehicleInsightCommand command) {
        validateApiKey();
        var payload = buildPayload(command);
        var systemPrompt = "Eres el asistente de analíticas de conducción y mantenimiento de Safecar. " +
                "Analiza los datos de sensores y responde en JSON válido respetando el esquema solicitado.";
        var userPrompt = buildUserPrompt(command);

        var requestBody = Map.of(
                "model", model,
                "temperature", 0.2,
                "response_format", buildResponseFormat(),
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt),
                        Map.of("role", "user", "content", payload)
                )
        );

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        try {
            var response = insightsRestTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody, headers),
                    ChatCompletionResponse.class
            );

            var body = Objects.requireNonNull(response.getBody(), "OpenAI response body cannot be null");
            var message = body.choices().isEmpty() ? null : body.choices().get(0).message();
            if (message == null) {
                throw new IllegalStateException("OpenAI response did not contain any choices");
            }
            var telemetryResponse = objectMapper.readValue(message.content(), TelemetryLLMResponse.class);
            return mapToResult(telemetryResponse, body.id());
        } catch (RestClientException | JsonProcessingException ex) {
            LOGGER.error("Unable to analyze telemetry with OpenAI", ex);
            throw new TelemetryAnalysisException("Unable to analyze telemetry with OpenAI", ex);
        }
    }

    private void validateApiKey() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new TelemetryAnalysisException("Missing OpenAI API key. Configure openai.api.key property.");
        }
    }

    private String buildUserPrompt(GenerateVehicleInsightCommand command) {
        return """
                Genera analíticas y recomendaciones para:
                - Conductor: %s
                - Vehículo (placa %s)
                Devuelve mantenimiento predictivo y hábitos de conducción.
                """.formatted(command.vehicle().driverFullName(), command.vehicle().plateNumber());
    }

    private String buildPayload(GenerateVehicleInsightCommand command) {
        var sensor = command.sensorPayload();
        var tireJson = sensor.tirePressures() != null ? sensor.tirePressures() : Collections.<String, BigDecimal>emptyMap();
        var payload = new LinkedHashMap<String, Object>();
        payload.put("captured_at", sensor.capturedAt());
        payload.put("severity", sensor.alertSeverity());
        payload.put("speed_kmh", sensor.speedKmh());
        payload.put("location", Map.of(
                "lat", sensor.latitude(),
                "lng", sensor.longitude()
        ));
        payload.put("tire_pressure", tireJson);
        payload.put("cabin_gas_ppm", sensor.cabinGasPpm());
        payload.put("cabin_gas_type", sensor.cabinGasType());
        payload.put("acceleration", Map.of(
                "lateral_g", sensor.lateralG(),
                "longitudinal_g", sensor.longitudinalG(),
                "vertical_g", sensor.verticalG()
        ));

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize telemetry payload", e);
        }
    }

    private Map<String, Object> buildResponseFormat() {
        var recommendationItemProperties = Map.of(
                "title", Map.of("type", "string"),
                "detail", Map.of("type", "string")
        );

        var recommendationItemSchema = new LinkedHashMap<String, Object>();
        recommendationItemSchema.put("type", "object");
        recommendationItemSchema.put("properties", recommendationItemProperties);
        recommendationItemSchema.put("required", List.of("title", "detail"));
        recommendationItemSchema.put("additionalProperties", false);

        var properties = new LinkedHashMap<String, Object>();
        properties.put("risk_level", Map.of("type", "string"));
        properties.put("maintenance_summary", Map.of("type", "string"));
        properties.put("maintenance_window", Map.of("type", "string"));
        properties.put("driving_habit_score", Map.of("type", "integer"));
        properties.put("driving_habit_summary", Map.of("type", "string"));
        properties.put("driving_alerts", Map.of("type", "string"));
        properties.put("recommendations", Map.of(
                "type", "array",
                "items", recommendationItemSchema
        ));

        var schema = new LinkedHashMap<String, Object>();
        schema.put("type", "object");
        schema.put("properties", properties);
        schema.put("required", List.of(
                "risk_level",
                "maintenance_summary",
                "maintenance_window",
                "driving_habit_score",
                "driving_habit_summary",
                "driving_alerts",
                "recommendations"
        ));
        schema.put("additionalProperties", false);

        return Map.of(
                "type", "json_schema",
                "json_schema", Map.of(
                        "name", "vehicle_telemetry_analysis",
                        "schema", schema,
                        "strict", true
                )
        );
    }

    private TelemetryInsightResult mapToResult(TelemetryLLMResponse response, String responseId) {
        var recommendations = response.recommendations() == null ? List.<InsightRecommendation>of()
                : response.recommendations().stream()
                .map(r -> new InsightRecommendation(r.title(), r.detail()))
                .toList();

        return new TelemetryInsightResult(
                response.riskLevel(),
                response.maintenanceSummary(),
                response.maintenanceWindow(),
                response.drivingHabitScore(),
                response.drivingHabitSummary(),
                response.drivingAlerts(),
                recommendations,
                responseId,
                Instant.now()
        );
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ChatCompletionResponse(
            String id,
            List<Choice> choices
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Choice(
            Message message
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Message(
            String role,
            String content
    ) {
    }

    private record TelemetryLLMResponse(
            @JsonProperty("risk_level") String riskLevel,
            @JsonProperty("maintenance_summary") String maintenanceSummary,
            @JsonProperty("maintenance_window") String maintenanceWindow,
            @JsonProperty("driving_habit_score") Integer drivingHabitScore,
            @JsonProperty("driving_habit_summary") String drivingHabitSummary,
            @JsonProperty("driving_alerts") String drivingAlerts,
            List<RecommendationPayload> recommendations
    ) {
    }

    private record RecommendationPayload(
            String title,
            String detail
    ) {
    }
}
