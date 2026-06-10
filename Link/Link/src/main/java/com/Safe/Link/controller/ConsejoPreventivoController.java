package com.Safe.Link.controller;

import com.Safe.Link.DTO.ConsejoPreventivoDTO;
import com.Safe.Link.service.ConsejoPreventivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/educacion/consejos")
@RequiredArgsConstructor
public class ConsejoPreventivoController {
    private final ConsejoPreventivoService service;

    @GetMapping
    public ResponseEntity<List<ConsejoPreventivoDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ConsejoPreventivoDTO>> filtrarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.filtrarPorTipo(tipo));
    }

    @PostMapping
    public ResponseEntity<ConsejoPreventivoDTO> crear(@RequestBody ConsejoPreventivoDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
