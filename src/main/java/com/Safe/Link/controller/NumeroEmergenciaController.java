package com.Safe.Link.controller;

import com.Safe.Link.DTO.NumeroEmergenciaDTO;
import com.Safe.Link.service.NumeroEmergenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergencia/numeros")
@RequiredArgsConstructor
public class NumeroEmergenciaController {
    private final NumeroEmergenciaService service;

    @GetMapping
    public ResponseEntity<List<NumeroEmergenciaDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NumeroEmergenciaDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<NumeroEmergenciaDTO> crear(@RequestBody NumeroEmergenciaDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NumeroEmergenciaDTO> actualizar(@PathVariable Long id, @RequestBody NumeroEmergenciaDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
