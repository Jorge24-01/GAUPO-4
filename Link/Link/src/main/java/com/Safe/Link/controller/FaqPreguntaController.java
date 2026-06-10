package com.Safe.Link.controller;

import com.Safe.Link.DTO.FaqPreguntaDTO;
import com.Safe.Link.service.FaqPreguntaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/soporte/faq")
@RequiredArgsConstructor
public class FaqPreguntaController {
    private final FaqPreguntaService service;

    @GetMapping
    public ResponseEntity<List<FaqPreguntaDTO>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<FaqPreguntaDTO>> filtrarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(service.filtrarPorCategoria(categoria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FaqPreguntaDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<FaqPreguntaDTO> crear(@RequestBody FaqPreguntaDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FaqPreguntaDTO> actualizar(@PathVariable Long id, @RequestBody FaqPreguntaDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
