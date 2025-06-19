package com.robson.ingles.service;

import com.robson.ingles.dto.ComandoResposta;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MensagemService {

    public ComandoResposta interpretarMensagem(String mensagem) {
        String texto = mensagem.trim().toLowerCase();

        // Respostas fixas
        Map<String, String> comandosFixos = new HashMap<>();
        comandosFixos.put("/ajuda", """
                ✅ *Comandos disponíveis*:
                /menu - Mostrar opções interativas
                /noticia - Ler notícias em inglês
                /traduza - Traduzir um texto
                /corrigir - Corrigir uma frase
                /explicar - Explicar algo em inglês
                /pronuncia - Saber como pronunciar
                /exemplo - Gerar uma frase de exemplo
                /resumir - Resumir um texto
                /conversar - Conversar em inglês com explicação
                /ebook - Sugestão de eBooks gratuitos
                """);

        comandosFixos.put("/menu", """
                📚 *Menu Interativo* (responda com o número):

                1️⃣ /noticia - Ler uma notícia em inglês
                2️⃣ /traduza - Traduzir um texto
                3️⃣ /corrigir - Corrigir frase em inglês
                4️⃣ /explicar - Explicar uma palavra/frase
                5️⃣ /pronuncia - Explicação da pronúncia
                6️⃣ /exemplo - Gerar frase de exemplo
                7️⃣ /resumir - Resumir um texto
                8️⃣ /conversar - Frase de conversa + explicação
                9️⃣ /ajuda - Ver todos os comandos
                """);

        comandosFixos.put("/noticia", "📰 Veja notícias em inglês: https://news.google.com/topstories?hl=en");
        comandosFixos.put("/ebook", "📖 Leia eBooks gratuitos em inglês: https://www.gutenberg.org/");

        // Retorno de comandos fixos
        for (Map.Entry<String, String> entry : comandosFixos.entrySet()) {
            if (texto.startsWith(entry.getKey())) {
                return ComandoResposta.respostaPronta(entry.getValue());
            }
        }

        // Comandos numéricos do menu interativo
        switch (texto) {
            case "1" -> { return ComandoResposta.requisicaoIA("noticia", "Leia uma notícia em inglês."); }
            case "2" -> { return ComandoResposta.requisicaoIA("traduza", ""); }
            case "3" -> { return ComandoResposta.requisicaoIA("corrigir", ""); }
            case "4" -> { return ComandoResposta.requisicaoIA("explicar", ""); }
            case "5" -> { return ComandoResposta.requisicaoIA("pronuncia", ""); }
            case "6" -> { return ComandoResposta.requisicaoIA("exemplo", ""); }
            case "7" -> { return ComandoResposta.requisicaoIA("resumir", ""); }
            case "8" -> { return ComandoResposta.requisicaoIA("conversar", ""); }
            case "9" -> { return ComandoResposta.respostaPronta(comandosFixos.get("/ajuda")); }
        }

        // Comandos que usam IA (via prefixo)
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

        return ComandoResposta.respostaPronta("❌ Comando não reconhecido. Digite /ajuda para ver as opções.");
    }

    private String removerComando(String texto, String comando) {
        return texto.replaceFirst("^" + comando + "\\s*", "").trim();
    }
}
