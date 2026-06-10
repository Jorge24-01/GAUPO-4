package com.Safe.Link.controller;

import com.Safe.Link.DTO.ReporteProblemaDTO;
import com.Safe.Link.entities.ReporteProblema;
import com.Safe.Link.service.ReporteProblemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/soporte/reportes")
@RequiredArgsConstructor
public class ReporteProblemaController {
    private final ReporteProblemaService service;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ReporteProblemaDTO dto) {
        try {
            ReporteProblema reporte = service.crear(dto);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Gracias por tu reporte. Nuestro equipo lo revisará pronto",
                    "id", reporte.getId()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReporteProblema>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.listarPorUsuario(usuarioId));
    }

    @GetMapping
    public ResponseEntity<List<ReporteProblema>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReporteProblema>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.listarPorEstado(estado));
    }
}
