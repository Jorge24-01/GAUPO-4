package com.examen.partelorenzo.controllers;

import com.examen.partelorenzo.Servicies.AlertaService;
import com.examen.partelorenzo.entities.Alerta;
import com.examen.partelorenzo.entities.Mensaje_de_chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {
    @Autowired
    private AlertaService alertaService;
    @PostMapping
 public ResponseEntity<?> salvar(@RequestBody Mensaje_de_chat chat){
        Alerta alerta = new Alerta();
        return new ResponseEntity<>(alerta, HttpStatus.CREATED);

    }

}
