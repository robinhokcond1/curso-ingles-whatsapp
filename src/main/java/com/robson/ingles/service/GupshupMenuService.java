package com.robson.ingles.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class GupshupMenuService {

    @Value("${gupshup.api.key}")
    private String gupshupApiKey;

    @Value("${gupshup.app.name}")
    private String appName;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String TEMPLATE_URL = "https://api.gupshup.io/sm/api/v1/template/msg";

    public void enviarMenu(String numeroDestino) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("apikey", gupshupApiKey);

            // Parâmetros do template
            Map<String, String> body = Map.of(
                    "channel", "whatsapp",
                    "source", appName,
                    "destination", numeroDestino,
                    "src.name", appName,
                    "template", "menu_interativo",
                    "template.params", "Menu de opções"
            );

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(TEMPLATE_URL, entity, String.class);

            log.info("📲 Menu inicial enviado via Gupshup para {}!", numeroDestino);
            log.debug("📝 Resposta Gupshup: {}", response.getBody());

        } catch (Exception e) {
            log.error("❌ Erro ao enviar menu interativo via Gupshup: {}", e.getMessage());
        }
    }
}
