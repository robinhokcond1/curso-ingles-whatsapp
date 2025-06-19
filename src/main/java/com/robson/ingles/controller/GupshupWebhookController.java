package com.robson.ingles.controller;

import com.robson.ingles.dto.ComandoResposta;
import com.robson.ingles.service.GupshupSenderService;
import com.robson.ingles.service.MensagemService;
import com.robson.ingles.service.interfaces.IAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/webhook/gupshup")
@RequiredArgsConstructor
public class GupshupWebhookController {

    private final IAiService aiService;
    private final MensagemService mensagemService;
    private final GupshupSenderService gupshupSenderService;

    @GetMapping
    public ResponseEntity<String> testWebhook() {
        return ResponseEntity.ok("✅ Webhook ativo!");
    }

    @PostMapping
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> payload) {
        log.info("📨 Payload bruto recebido do Gupshup:");
        payload.forEach((k, v) -> log.info(k + ": " + v));

        try {
            String type = (String) payload.get("type");
            if (!"message".equals(type)) {
                log.warn("⚠️ Evento ignorado: tipo {}", type);
                return ResponseEntity.ok("Evento ignorado.");
            }

            Map<String, Object> messagePayload = (Map<String, Object>) payload.get("payload");
            if (messagePayload == null || !"text".equals(messagePayload.get("type"))) {
                log.warn("⚠️ Tipo de mensagem não suportado.");
                return ResponseEntity.ok("Tipo de mensagem não suportado.");
            }

            Map<String, Object> innerPayload = (Map<String, Object>) messagePayload.get("payload");
            Map<String, Object> senderInfo = (Map<String, Object>) messagePayload.get("sender");

            String textoRecebido = (String) innerPayload.get("text");
            String remetente = (String) senderInfo.get("phone");

            if (textoRecebido == null || remetente == null) {
                log.warn("⚠️ Texto ou número do remetente ausente.");
                return ResponseEntity.ok("Dados incompletos.");
            }

            log.info("📥 [{}] -> {}", remetente, textoRecebido);
            ComandoResposta comando = mensagemService.interpretarMensagem(textoRecebido);

            String resposta = comando.isUsarIA()
                    ? aiService.ask(comando.getConteudo(), comando.getTipo())
                    : comando.getConteudo();

            gupshupSenderService.enviarTexto(remetente, resposta);

            log.info("📤 Resposta enviada: {}", resposta);
            return ResponseEntity.ok("Mensagem processada com sucesso.");

        } catch (Exception e) {
            log.error("❌ Erro ao processar mensagem", e);
            return ResponseEntity.ok("Erro ao processar mensagem.");
        }
    }
}
