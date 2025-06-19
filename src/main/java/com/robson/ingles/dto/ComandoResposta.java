package com.robson.ingles.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ComandoResposta {

    private final boolean usarIA;
    private final String tipo;
    private final String conteudo;

    // Método de fábrica para comandos que exigem IA (ex: /traduza, /corrigir, /conversar, etc.)
    public static ComandoResposta requisicaoIA(String tipo, String conteudo) {
        return new ComandoResposta(true, tipo, conteudo);
    }

    // Método de fábrica para comandos de resposta direta/fixa (ex: /ajuda, /menu, etc.)
    public static ComandoResposta respostaPronta(String resposta) {
        return new ComandoResposta(false, "resposta_pronta", resposta);
    }
}
