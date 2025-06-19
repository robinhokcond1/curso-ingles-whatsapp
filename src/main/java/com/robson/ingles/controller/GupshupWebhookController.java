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
        System.out.println("📨 Payload bruto recebido do Gupshup:");
        payload.forEach((k, v) -> System.out.println(k + ": " + v));

        try {
            String type = (String) payload.get("type");
            if (!"message".equals(type)) {
                System.out.println("⚠️ Ignorando evento do tipo: " + type);
                return ResponseEntity.ok("Evento ignorado.");
            }

            // Corrigido aqui: buscar text dentro de payload.payload.text
            Map<String, Object> innerPayload = (Map<String, Object>) payload.get("payload");
            String text = (String) innerPayload.get("text");

            if (text == null || text.trim().isEmpty()) {
                System.out.println("⚠️ Texto não encontrado no payload.");
                return ResponseEntity.ok("Sem texto para processar.");
            }

            System.out.println("➡️ Texto extraído: " + text);

            String respostaGPT = openAIService.ask(text);
            System.out.println("⬅️ Resposta do GPT: " + respostaGPT);

            return ResponseEntity.ok(respostaGPT);
        } catch (Exception e) {
            System.out.println("❌ Erro ao processar mensagem: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok("Erro ao processar a mensagem.");
        }
    }

}
