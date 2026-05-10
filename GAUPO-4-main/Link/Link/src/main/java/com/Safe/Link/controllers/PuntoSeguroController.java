package com.Safe.Link.controllers;

import com.Safe.Link.entities.PuntoSeguro;
import com.Safe.Link.services.PuntoSeguroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/puntoseguro")
public class PuntoSeguroController {

    private final PuntoSeguroService puntoSeguroService;

    public PuntoSeguroController(PuntoSeguroService puntoSeguroService) {
        this.puntoSeguroService = puntoSeguroService;
    }

    @PostMapping
    public ResponseEntity<PuntoSeguro> guardar(@RequestBody PuntoSeguro puntoSeguro) {
        try {
            PuntoSeguro nuevo = puntoSeguroService.guardar(puntoSeguro);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo); // 201
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // 400
        }
    }

    @GetMapping
    public ResponseEntity<List<PuntoSeguro>> listar() {
        try {
            return ResponseEntity.ok(puntoSeguroService.listar()); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuntoSeguro> buscarPorId(@PathVariable Long id) {
        try {
            PuntoSeguro punto = puntoSeguroService.buscarPorId(id);

            if (punto == null) {
                return ResponseEntity.notFound().build(); // 404
            }

            return ResponseEntity.ok(punto); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PuntoSeguro>> listarPorUsuario(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(puntoSeguroService.listarPorUsuario(idUsuario)); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            PuntoSeguro punto = puntoSeguroService.buscarPorId(id);

            if (punto == null) {
                return ResponseEntity.notFound().build(); // 404
            }

            puntoSeguroService.eliminar(id);
            return ResponseEntity.ok().build(); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }
}
