package com.examen.partelorenzo.controllers;

import com.examen.partelorenzo.entities.Mensaje_de_chat;
import com.examen.partelorenzo.repositories.Mensaje_de_chat_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class MensajeChatController {

    @Autowired
    private Mensaje_de_chat_repository repository;

    @GetMapping
    public List<Mensaje_de_chat> obtenerTodos(){
        return repository.findAll();
    }
    @PostMapping
    public Mensaje_de_chat salvar(@RequestBody Mensaje_de_chat chat){
        return repository.save(chat);
    }
}
