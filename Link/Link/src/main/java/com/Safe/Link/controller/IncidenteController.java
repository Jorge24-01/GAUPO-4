package com.Safe.Link.controller;

import com.Safe.Link.DTO.IncidenteDTO;
import com.Safe.Link.service.IncidenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidente")
@RequiredArgsConstructor
public class IncidenteController {
    private final IncidenteService service;

    @PostMapping
    public ResponseEntity<IncidenteDTO>create(@RequestBody IncidenteDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<IncidenteDTO>>getByEstado(@PathVariable String estado){
        return ResponseEntity.ok(service.getByEstado(estado));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<IncidenteDTO>>getByUsuario(@PathVariable Long idUsuario){
        return ResponseEntity.ok(service.getByUsuario(idUsuario));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<IncidenteDTO>>getByTipo(@PathVariable String tipo){
        return ResponseEntity.ok(service.getByTipo(tipo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidenteDTO> update(@PathVariable Long id, @RequestBody IncidenteDTO dto){
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
