package com.robson.ingles;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook/gupshup")
public class GupshupWebhookController {

    @PostMapping
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> payload) {
        System.out.println("Mensagem recebida do Gupshup: " + payload);
        return ResponseEntity.ok("Mensagem recebida com sucesso!");
    }
}