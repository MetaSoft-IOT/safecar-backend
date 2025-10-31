package com.safecar.platform.insights.application.internal.outboundservices.acl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIInsightsClient implements InsightsLLMClient {

  private final RestTemplate http = new RestTemplate();

  @Value("${openai.api.key}") String apiKey;
  @Value("${openai.model: gpt-4.1-mini}") String model;

  @Override
  public LLMScoreResult scoreFromTelemetry(String telemetryWindowJson) {
    // Prompt minimal: tú puedes enriquecerlo
    String prompt = """
      You are a driving-safety analyst. Given normalized telemetry (JSON: trips, speed, harshEvents, braking, nightDriving, phoneUsage),
      return ONLY a JSON: {"score":0-100, "explanation":"short"}
      Telemetry:
      """ + telemetryWindowJson;

    var body = """
      { "model":"%s", "messages":[{"role":"user","content":%s}], "response_format":{"type":"json_object"} }
      """.formatted(model, jsonQuote(prompt));

    var resp = callOpenAI(body);
    // parse min: busca "score" y "explanation"
    int score = Json.minParseInt(resp, "score", 70);
    String explanation = Json.minParseString(resp, "explanation", "No explanation");
    return new LLMScoreResult(score, explanation);
  }

  @Override
  public String generateRecommendations(String summaryJson) {
    String prompt = """
      You are an auto maintenance and safe-driving coach.
      Based on this summary JSON (driver habits, vehicle diagnostics, mileage, climate):
      produce 3-5 personalized, actionable recommendations.
      Output plain text with bullet points, each 1–2 lines, avoid alarmist tone.
      Summary:
      """ + summaryJson;

    var body = """
      { "model":"%s", "messages":[{"role":"user","content":%s}] }
      """.formatted(model, jsonQuote(prompt));

    return callOpenAI(body);
  }

  // ---------- helpers ----------
  private String callOpenAI(String reqBody) {
    HttpHeaders h = new HttpHeaders();
    h.setContentType(MediaType.APPLICATION_JSON);
    h.setBearerAuth(apiKey);
    var entity = new HttpEntity<>(reqBody, h);
    ResponseEntity<String> resp = http.postForEntity("https://api.openai.com/v1/chat/completions", entity, String.class);
    return resp.getBody(); // simplificado: en prod, parsea choices[0].message.content
  }

  private static String jsonQuote(String s){ return ("\"" + s.replace("\\","\\\\").replace("\"","\\\"") + "\""); }

  /** util minimal (puedes reemplazar por Jackson) */
  static class Json {
    static int minParseInt(String s, String key, int def){
      try{
        var m = java.util.regex.Pattern.compile("\""+key+"\"\\s*:\\s*(\\d+)").matcher(s);
        return m.find()? Integer.parseInt(m.group(1)) : def;
      }catch(Exception e){ return def; }
    }
    static String minParseString(String s, String key, String def){
      try{
        var m = java.util.regex.Pattern.compile("\""+key+"\"\\s*:\\s*\"([^\"]*)\"").matcher(s);
        return m.find()? m.group(1) : def;
      }catch(Exception e){ return def; }
    }
  }
}
