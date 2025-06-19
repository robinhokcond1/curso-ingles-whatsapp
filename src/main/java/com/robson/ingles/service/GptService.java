package com.robson.ingles.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GptService {

    @Value("${ai.provider}")
    private String provider;

    @Value("${openai.api.key:}")
    private String openaiKey;

    @Value("${gemini.api.key:}")
    private String geminiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public String enviarMensagemParaGpt(String prompt, String tipo) {
        if ("gemini".equalsIgnoreCase(provider)) {
            return enviarParaGemini(prompt, tipo);
        } else {
            return enviarParaOpenAI(prompt, tipo);
        }
    }

    private String enviarParaOpenAI(String prompt, String tipo) {
        String systemPrompt = tipo.equals("corrigir")
                ? "Você é um professor de inglês. Corrija a frase a seguir e explique se necessário."
                : "Traduza o seguinte texto para português do Brasil.";

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", prompt)
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(OPENAI_URL, HttpMethod.POST, request, Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return (String) message.get("content");
    }

    private String enviarParaGemini(String prompt, String tipo) {
        String instruction = tipo.equals("corrigir")
                ? "Corrija e explique a seguinte frase:"
                : "Traduza o seguinte texto para português do Brasil:";

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", instruction + "\n" + prompt)
                        ))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        String fullUrl = GEMINI_URL + geminiKey;

        ResponseEntity<Map> response = restTemplate.exchange(fullUrl, HttpMethod.POST, request, Map.class);

        Map<String, Object> candidate = ((List<Map<String, Object>>) response.getBody().get("candidates")).get(0);
        Map<String, Object> content = (Map<String, Object>) candidate.get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

        return parts.get(0).get("text").toString();
    }
}
