package com.robson.ingles.controller;

import com.robson.ingles.service.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook/gupshup")
public class WebhookController {

    @Autowired
    private MensagemService mensagemService;

    @PostMapping
    public ResponseEntity<String> receberMensagem(@RequestBody Map<String, Object> payload) {
        try {
            Map<String, Object> message = (Map<String, Object>) payload.get("message");
            String texto = (String) message.get("text");

            String resposta = mensagemService.processarMensagem(texto);
            return ResponseEntity.ok(resposta);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Erro ao processar a mensagem. Por favor, tente novamente mais tarde.");
        }
    }

    @GetMapping
    public ResponseEntity<String> testar() {
        return ResponseEntity.ok("Webhook Gupshup ativo!");
    }
}
