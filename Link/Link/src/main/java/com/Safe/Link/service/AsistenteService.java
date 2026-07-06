package com.Safe.Link.service;

import com.Safe.Link.DTO.AsistenteRequestDTO;
import com.Safe.Link.DTO.AsistenteResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AsistenteService {

    private static final String SYSTEM_PROMPT = "Eres el Asistente SafeLink. Responde en espa\u00f1ol, de forma clara, breve y \u00fatil. Ayudas con preparaci\u00f3n ante emergencias, kit de emergencia, zonas seguras, rutas de evacuaci\u00f3n, registro familiar y uso de la plataforma SafeLink. Si no est\u00e1s seguro, recomienda consultar fuentes oficiales o servicios de emergencia.";
    private static final String CONFIGURACION_FALTANTE = "No se encontr\u00f3 la configuraci\u00f3n del asistente.";
    private static final String ERROR_COMUNICACION = "No pude comunicarme con el asistente en este momento. Int\u00e9ntalo nuevamente.";
    private static final String PREGUNTA_VACIA = "Por favor ingresa una pregunta para el asistente.";

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${groq.api.key:}")
    private String groqApiKey;

    @Value("${groq.model:llama-3.1-8b-instant}")
    private String groqModel;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String groqApiUrl;

    public AsistenteResponseDTO preguntar(AsistenteRequestDTO request) {
        String pregunta = request == null ? "" : request.getPregunta();
        if (pregunta == null || pregunta.trim().isEmpty()) {
            return new AsistenteResponseDTO(PREGUNTA_VACIA);
        }

        if (groqApiKey == null || groqApiKey.isBlank()) {
            return new AsistenteResponseDTO(CONFIGURACION_FALTANTE);
        }

        try {
            String respuesta = consultarGroq(pregunta.trim());
            if (respuesta == null || respuesta.isBlank()) {
                return new AsistenteResponseDTO(ERROR_COMUNICACION);
            }
            return new AsistenteResponseDTO(respuesta);
        } catch (Exception ex) {
            return new AsistenteResponseDTO(ERROR_COMUNICACION);
        }
    }

    private String consultarGroq(String pregunta) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(groqApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("model", groqModel);
        body.put("messages", List.of(
                Map.of("role", "system", "content", SYSTEM_PROMPT),
                Map.of("role", "user", "content", pregunta)
        ));
        body.put("temperature", 0.4);
        body.put("max_tokens", 500);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(groqApiUrl, HttpMethod.POST, entity, String.class);

        return extraerContenido(response.getBody());
    }

    private String extraerContenido(String responseBody) throws Exception {
        if (responseBody == null || responseBody.isBlank()) {
            return "";
        }

        JsonNode root = objectMapper.readTree(responseBody);
        return root.path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asText("");
    }
}
