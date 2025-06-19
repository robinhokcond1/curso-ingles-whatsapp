package com.robson.ingles.service;

import com.robson.ingles.service.interfaces.IAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResilientAIService implements IAiService {

    private final OpenAIService openAIService;
    private final GeminiService geminiService;

    @Override
    public String ask(String prompt, String tipo) {
        try {
            return openAIService.ask(prompt, tipo);
        } catch (Exception e) {
            log.warn("⚠️ Falha no OpenAI para tipo '{}'. Tentando Gemini. Erro: {}", tipo, e.getMessage());
            try {
                return geminiService.ask(prompt, tipo);
            } catch (Exception ex) {
                log.error("❌ Falha em ambos os provedores (OpenAI & Gemini) para tipo '{}': {}", tipo, ex.getMessage());
                return "Desculpe, ocorreu um erro ao processar sua mensagem.";
            }
        }
    }
}
