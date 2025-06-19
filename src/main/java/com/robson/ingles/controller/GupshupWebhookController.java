package com.robson.ingles.controller;

import com.robson.ingles.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        try {
            Map<String, Object> messagePayload = (Map<String, Object>) payload.get("payload");
            String message = (String) messagePayload.get("text");

            String respostaGPT = openAIService.ask(message);
            System.out.println("➡️ Pergunta: " + message);
            System.out.println("⬅️ Resposta do GPT: " + respostaGPT);

            return ResponseEntity.ok(respostaGPT);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Erro ao processar a mensagem.");
        }
    }
}
