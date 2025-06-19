package com.robson.ingles.service.interfaces;


public interface IAiService {
    /**
     * Processa uma mensagem com base no tipo de comando (ex: "corrigir", "traduza", "conversar").
     *
     * @param prompt conteúdo da mensagem do usuário
     * @param tipo tipo do comando
     * @return resposta da IA (OpenAI ou Gemini)
     */
    String ask(String prompt, String tipo);
}


