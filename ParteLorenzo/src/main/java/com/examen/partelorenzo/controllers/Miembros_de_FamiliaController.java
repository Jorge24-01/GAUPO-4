package com.examen.partelorenzo.controllers;

import com.examen.partelorenzo.Servicies.Miembros_de_Familia_service;
import com.examen.partelorenzo.entities.Miembros_de_Familia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public class Miembros_de_FamiliaController {
    @Autowired
    private Miembros_de_Familia_service miembros_de_Familia_service;

    @PostMapping
    public ResponseEntity<Miembros_de_Familia> agregar_miembro(@RequestBody Miembros_de_Familia miembros) {
        Miembros_de_Familia nuevoMienbro = new Miembros_de_Familia();
        return new ResponseEntity<>(nuevoMienbro, HttpStatus.CREATED);
    }

}
