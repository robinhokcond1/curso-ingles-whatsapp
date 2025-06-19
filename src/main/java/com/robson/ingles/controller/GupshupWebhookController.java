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
        log.info("📩 Payload recebido: {}", payload);

        try {
            if (!"message".equals(payload.get("type"))) {
                log.info("⚠️ Ignorando evento do tipo '{}'", payload.get("type"));
                return ResponseEntity.ok("Evento ignorado");
            }

            Map<String, Object> message = (Map<String, Object>) payload.get("payload");
            String text = (String) message.get("text");

            log.info("➡️ Texto extraído: {}", text);
            String respostaGPT = openAIService.ask(text);
            log.info("✅ Resposta do GPT: {}", respostaGPT);

            return ResponseEntity.ok(respostaGPT);
        } catch (Exception e) {
            log.error("❌ Erro ao processar mensagem: ", e);
            return ResponseEntity.ok("Erro ao processar a mensagem.");
        }
    }

}
