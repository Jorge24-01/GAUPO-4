package com.Safe.Link.controller;

import com.Safe.Link.DTO.GuiaEducativaDTO;
import com.Safe.Link.service.GuiaEducativaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest; // us -29 imports
import com.Safe.Link.DTO.CompartirGuiaDTO; // us -29 imports



import java.util.List;

@RestController
@RequestMapping("/api/educacion/guias")
@RequiredArgsConstructor
public class GuiaEducativaController {
    private final GuiaEducativaService service;

    @GetMapping
    public ResponseEntity<List<GuiaEducativaDTO>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/offline")
    public ResponseEntity<List<GuiaEducativaDTO>> listarOffline() {
        return ResponseEntity.ok(service.listarOffline());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuiaEducativaDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
    // US-29 — Escenarios 1 y 2: obtener payload para compartir tutorial
// GET público: cualquier usuario autenticado o no puede recibir el enlace
    @GetMapping("/{id}/compartir")
    public ResponseEntity<CompartirGuiaDTO> compartir(
            @PathVariable Long id,
            HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName()
                + (request.getServerPort() != 80 && request.getServerPort() != 443
                ? ":" + request.getServerPort() : "");
        return ResponseEntity.ok(service.generarEnlaceCompartir(id, baseUrl));
    }
    @GetMapping("/filtrar")
    public ResponseEntity<List<GuiaEducativaDTO>> filtrar(
            @RequestParam(required = false) String tipoDesastre,
            @RequestParam(required = false) String fase,
            @RequestParam(required = false) String categoria) {

        if (tipoDesastre != null && fase != null) {
            return ResponseEntity.ok(service.filtrarPorTipoYFase(tipoDesastre, fase));
        } else if (tipoDesastre != null) {
            return ResponseEntity.ok(service.filtrarPorTipoDesastre(tipoDesastre));
        } else if (fase != null) {
            return ResponseEntity.ok(service.filtrarPorFase(fase));
        } else if (categoria != null) {
            return ResponseEntity.ok(service.filtrarPorCategoria(categoria));
        }
        return ResponseEntity.ok(service.listarTodas());
    }

    @PostMapping
    public ResponseEntity<GuiaEducativaDTO> crear(@RequestBody GuiaEducativaDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuiaEducativaDTO> actualizar(@PathVariable Long id, @RequestBody GuiaEducativaDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
