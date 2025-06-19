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
        payload.forEach((k, v) -> System.out.println("🔹 " + k + ": " + v));

        try {
            Map<String, Object> message;

            if (payload.containsKey("message")) {
                message = (Map<String, Object>) payload.get("message");
                System.out.println("📦 Usando 'message' como chave.");
            } else if (payload.containsKey("payload")) {
                message = (Map<String, Object>) payload.get("payload");
                System.out.println("📦 Usando 'payload' como chave.");
            } else {
                System.out.println("❌ Nenhuma chave esperada encontrada no payload.");
                return ResponseEntity.ok("Formato de payload não suportado.");
            }

            message.forEach((k, v) -> System.out.println("📝 Campo do message: " + k + " => " + v));

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
