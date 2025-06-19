package com.robson.ingles.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class GupshupSenderService {

    private static final String GUPSHUP_URL = "https://api.gupshup.io/wa/api/v1/msg";

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

            String payloadMensagem = String.format("{\"type\":\"text\",\"text\":%s}", wrapJson(mensagem));

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("channel", "whatsapp");
            body.add("source", appName);
            body.add("destination", phoneNumber);
            body.add("message", payloadMensagem);
            body.add("src.name", appName);
            body.add("disablePreview", "false");
            body.add("encode", "false");

            // Logs para validação
            log.info("🔎 Corpo da requisição a ser enviado: {}", body);
            log.info("🔑 Header da requisição: Content-Type: {}", headers.getContentType());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(GUPSHUP_URL, request, String.class);

            log.info("✉️ Mensagem enviada via Gupshup para {}: {}", phoneNumber, mensagem);
            log.debug("📬 Resposta da Gupshup: {}", response.getBody());
        } catch (Exception e) {
            log.error("❌ Erro ao enviar mensagem para Gupshup: {}", e.getMessage(), e);
        }
    }

    // Método auxiliar para escapar corretamente o conteúdo do campo text no JSON
    private String wrapJson(String value) {
        return "\"" + value.replace("\"", "\\\"") + "\"";
    }
}
