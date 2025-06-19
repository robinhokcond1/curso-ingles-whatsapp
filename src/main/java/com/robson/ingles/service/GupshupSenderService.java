package com.robson.ingles.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GupshupSenderService {

    private static final String GUPSHUP_URL = "https://api.gupshup.io/sm/api/v1/msg";

    @Value("${gupshup.api.key:}")
    private String gupshupApiKey;

    @Value("${gupshup.app.name:CursoInglesIA}")
    private String appName;

    private final RestTemplate restTemplate = new RestTemplate();

    public void enviarTexto(String phoneNumber, String mensagem) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("apikey", gupshupApiKey);

            Map<String, String> body = new HashMap<>();
            body.put("channel", "whatsapp");
            body.put("source", appName);
            body.put("destination", phoneNumber);
            body.put("message", "{\"type\":\"text\",\"text\":\"" + mensagem + "\"}");
            body.put("src.name", appName);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(GUPSHUP_URL, request, String.class);

            log.info("✉️ Mensagem enviada via Gupshup para {}: {}", phoneNumber, mensagem);
            log.debug("Resposta da Gupshup: {}", response.getBody());
        } catch (Exception e) {
            log.error("❌ Erro ao enviar mensagem para Gupshup: {}", e.getMessage(), e);
        }
    }
}
