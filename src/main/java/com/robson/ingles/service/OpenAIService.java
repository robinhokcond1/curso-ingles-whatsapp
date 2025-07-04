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
public class OpenAIService {

    @Value("${openai.api.key:}")
    private String openaiApiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String ask(String prompt, String tipo) {
        String systemPrompt = gerarSystemPrompt(tipo);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    OPENAI_URL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);
            Map<String, Object> message = (Map<String, Object>) ((Map<?, ?>) ((List<?>) responseMap.get("choices")).get(0)).get("message");

            return message.get("content").toString();
        } catch (Exception e) {
            log.error("❌ Erro ao chamar OpenAI: {}", e.getMessage());
            throw new RuntimeException("Erro ao se comunicar com OpenAI", e);
        }
    }

    private String gerarSystemPrompt(String tipo) {
        return switch (tipo) {
            case "corrigir" -> "Você é um professor de inglês. Corrija a frase a seguir e explique se necessário.";
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
