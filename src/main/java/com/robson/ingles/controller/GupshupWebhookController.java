package com.robson.ingles.controller;

import com.robson.ingles.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/webhook/gupshup")
@RequiredArgsConstructor
public class GupshupWebhookController {

    private final OpenAIService openAIService;

    @GetMapping
    public ResponseEntity<String> testWebhook() {
        return ResponseEntity.ok("Webhook ativo!");
    }

    @PostMapping
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> payload) {
        log.info("📨 Payload bruto recebido do Gupshup:");
        payload.forEach((k, v) -> log.info(k + ": " + v));

        try {
            String type = (String) payload.get("type");
            if (!"message".equals(type)) {
                log.warn("⚠️ Ignorando evento do tipo: {}", type);
                return ResponseEntity.ok("Evento ignorado.");
            }

            Map<String, Object> messagePayload = (Map<String, Object>) payload.get("payload");

            if (messagePayload == null || !"text".equals(messagePayload.get("type"))) {
                log.warn("⚠️ Tipo de mensagem não suportado ou payload vazio.");
                return ResponseEntity.ok("Tipo de mensagem não suportado.");
            }

            Map<String, Object> innerPayload = (Map<String, Object>) messagePayload.get("payload");
            String text = (String) innerPayload.get("text");

            if (text == null || text.trim().isEmpty()) {
                log.warn("⚠️ Texto não encontrado no payload.");
                return ResponseEntity.ok("Sem texto para processar.");
            }

            log.info("➡️ Texto extraído: {}", text);

            String respostaGPT = openAIService.ask(text);
            log.info("⬅️ Resposta do GPT: {}", respostaGPT);

            return ResponseEntity.ok(respostaGPT);
        } catch (Exception e) {
            log.error("❌ Erro ao processar mensagem", e);
            return ResponseEntity.ok("Erro ao processar a mensagem.");
        }
    }

}
