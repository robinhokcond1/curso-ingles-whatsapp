package com.robson.ingles;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String home() {
        return "API de Curso de Inglês ativa!";
    }
    @PostMapping("/")
    public ResponseEntity<String> handleGupshupValidation() {
        return ResponseEntity.ok("POST recebido com sucesso!");
    }

}

