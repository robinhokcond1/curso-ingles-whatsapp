package com.robson.ingles.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MensagemService {

    public String processarMensagem(String mensagem) {
        String texto = mensagem.toLowerCase();

        Map<String, String> comandos = new HashMap<>();
        comandos.put("/ajuda", "Olá! Eu sou seu assistente de inglês. Você pode usar comandos como /menu, /noticia, /traduza, /corrigir e /ebook.");
        comandos.put("/menu", "📚 Menu:\n1. /noticia - Ler notícias em inglês\n2. /traduza - Traduza um texto\n3. /corrigir - Corrigir uma frase em inglês\n4. /ebook - Sugestões de leitura de eBooks gratuitos\n5. /ajuda - Obter ajuda com os comandos");
        comandos.put("/noticia", "📰 Acesse a seção de notícias atualizadas em inglês: https://news.google.com/topstories?hl=en");
        comandos.put("/traduza", "🔤 Envie o texto com o comando /traduza seguido da frase para tradução.\nExemplo: /traduza Bom dia, como vai você?");
        comandos.put("/corrigir", "📝 Envie a frase com o comando /corrigir para que eu sugira correções.\nExemplo: /corrigir He go to school every day.");
        comandos.put("/ebook", "📖 Confira esta plataforma com eBooks gratuitos em inglês: https://www.gutenberg.org/");

        for (String comando : comandos.keySet()) {
            if (texto.startsWith(comando)) {
                return comandos.get(comando);
            }
        }

        return "Desculpe, não reconheço esse comando. Digite /ajuda para ver as opções disponíveis.";
    }
}
