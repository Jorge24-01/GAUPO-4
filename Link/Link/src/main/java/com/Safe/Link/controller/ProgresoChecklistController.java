package com.Safe.Link.controller;

import com.Safe.Link.DTO.ProgresoChecklistDTO;
import com.Safe.Link.service.ProgresoChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/soporte/checklist")
@RequiredArgsConstructor
public class ProgresoChecklistController {
    private final ProgresoChecklistService service;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ProgresoChecklistDTO>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/completados")
    public ResponseEntity<List<ProgresoChecklistDTO>> obtenerCompletados(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.obtenerCompletados(usuarioId));
    }

    @PostMapping("/marcar")
    public ResponseEntity<ProgresoChecklistDTO> marcarItem(@RequestBody ProgresoChecklistDTO dto) {
        return ResponseEntity.ok(service.marcarItem(dto));
    }

    @PostMapping("/personalizado")
    public ResponseEntity<ProgresoChecklistDTO> agregarPersonalizado(@RequestBody ProgresoChecklistDTO dto) {
        return ResponseEntity.ok(service.agregarItemPersonalizado(dto));
    }

    @DeleteMapping("/usuario/{usuarioId}/item")
    public ResponseEntity<Void> eliminarItem(
            @PathVariable Long usuarioId,
            @RequestParam String nombreItem) {
        service.eliminarItemPersonalizado(usuarioId, nombreItem);
        return ResponseEntity.noContent().build();
    }
}
