package com.robson.ingles.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GeminiService {

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String ask(String prompt, String tipo) {
        String instruction = gerarInstrucao(tipo);
        String promptCompleto = instruction + "\n\n" + prompt;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", promptCompleto)))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        String fullUrl = GEMINI_URL + geminiApiKey;

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    fullUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");

            if (candidates == null || candidates.isEmpty()) {
                return "Desculpe, não recebi resposta do Gemini.";
            }

            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

            if (parts.isEmpty() || parts.get(0).get("text") == null) {
                return "Desculpe, texto não encontrado na resposta do Gemini.";
            }

            return parts.get(0).get("text").toString();

        } catch (Exception e) {
            log.error("❌ Erro ao chamar a API do Gemini: {}", e.getMessage());
            throw new RuntimeException("Erro ao se comunicar com Gemini", e);
        }
    }

    private String gerarInstrucao(String tipo) {
        return switch (tipo) {
            case "corrigir" -> "Corrija a frase a seguir e explique a correção se necessário.";
            case "traduza" -> "Traduza o seguinte texto para o português do Brasil.";
            case "explicar" -> "Explique de forma clara e didática o significado da frase ou palavra em inglês.";
            case "pronuncia" -> "Descreva como pronunciar corretamente a frase em inglês e forneça uma explicação textual.";
            case "exemplo" -> "Crie um exemplo de uso da palavra ou expressão em uma frase em inglês, com tradução.";
            case "resumir" -> "Resuma o texto a seguir de forma objetiva e simples.";
            case "conversar" -> "Gere uma frase comum em inglês para conversação e, abaixo, forneça uma explicação gramatical ou de vocabulário em português.";
            default -> "Você é um assistente educacional de inglês. Responda com clareza e de forma didática.";
        };
    }
}
