package com.Safe.Link.controller;

import com.Safe.Link.DTO.ReporteProblemaDTO;
import com.Safe.Link.entities.ReporteProblema;
import com.Safe.Link.service.ReporteProblemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/soporte/reportes")
@RequiredArgsConstructor
public class ReporteProblemaController {
    private final ReporteProblemaService service;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ReporteProblemaDTO dto) {
        ReporteProblema reporte = service.crear(dto);
        String mensaje = messageSource.getMessage("reporte.gracias", null, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(Map.of("mensaje", mensaje, "id", reporte.getId()));
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
