package com.robson.ingles.service;

import com.robson.ingles.dto.ComandoResposta;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MensagemService {

    public ComandoResposta interpretarMensagem(String mensagem) {
        String texto = mensagem.trim().toLowerCase();

        Map<String, String> comandosFixos = new HashMap<>();
        comandosFixos.put("/ajuda", "Olá! Eu sou seu assistente de inglês. Comandos: /menu, /noticia, /traduza, /corrigir, /explicar, /pronuncia, /exemplo, /resumir, /conversar, /ebook.");
        comandosFixos.put("/menu", "📚 Menu:\n1. /noticia\n2. /traduza\n3. /corrigir\n4. /explicar\n5. /pronuncia\n6. /exemplo\n7. /resumir\n8. /conversar\n9. /ebook\n10. /ajuda");
        comandosFixos.put("/noticia", "📰 Veja notícias em inglês: https://news.google.com/topstories?hl=en");
        comandosFixos.put("/ebook", "📖 Leia eBooks gratuitos em inglês: https://www.gutenberg.org/");

        for (Map.Entry<String, String> entry : comandosFixos.entrySet()) {
            if (texto.startsWith(entry.getKey())) {
                return ComandoResposta.respostaPronta(entry.getValue());
            }
        }

        // Comandos que exigem IA
        if (texto.startsWith("/corrigir")) {
            return ComandoResposta.requisicaoIA("corrigir", removerComando(texto, "/corrigir"));
        } else if (texto.startsWith("/traduza")) {
            return ComandoResposta.requisicaoIA("traduza", removerComando(texto, "/traduza"));
        } else if (texto.startsWith("/explicar")) {
            return ComandoResposta.requisicaoIA("explicar", removerComando(texto, "/explicar"));
        } else if (texto.startsWith("/pronuncia")) {
            return ComandoResposta.requisicaoIA("pronuncia", removerComando(texto, "/pronuncia"));
        } else if (texto.startsWith("/exemplo")) {
            return ComandoResposta.requisicaoIA("exemplo", removerComando(texto, "/exemplo"));
        } else if (texto.startsWith("/resumir")) {
            return ComandoResposta.requisicaoIA("resumir", removerComando(texto, "/resumir"));
        } else if (texto.startsWith("/conversar")) {
            return ComandoResposta.requisicaoIA("conversar", removerComando(texto, "/conversar"));
        }

        return ComandoResposta.respostaPronta("Desculpe, não reconheço esse comando. Digite /ajuda para ver as opções disponíveis.");
    }

    private String removerComando(String texto, String comando) {
        return texto.replaceFirst("^" + comando + "\\s*", "").trim();
    }
}
