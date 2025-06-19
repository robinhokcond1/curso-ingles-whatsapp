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
        System.out.println("📨 Payload bruto recebido do Gupshup:");
        payload.forEach((k, v) -> System.out.println(k + ": " + v));

        try {
            // Gupshup v2 normalmente envia em "message"
            Map<String, Object> message = (Map<String, Object>) payload.get("message");
            String text = (String) message.get("text");

            System.out.println("➡️ Texto extraído: " + text);

            String respostaGPT = openAIService.ask(text);
            System.out.println("⬅️ Resposta do GPT: " + respostaGPT);

            return ResponseEntity.ok(respostaGPT);
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao processar mensagem: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok("Erro ao processar a mensagem.");
        }
    }

}
