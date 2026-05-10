package com.Safe.Link.controller;

import com.Safe.Link.DTO.KitEmergenciDTO;
import com.Safe.Link.service.KitEmergenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kits")
@RequiredArgsConstructor
public class KitEmergenciaController {
    private final KitEmergenciaService service;

    @PostMapping
    public ResponseEntity<KitEmergenciDTO>create(@RequestBody KitEmergenciDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<KitEmergenciDTO> getByUsuario(@PathVariable Long idUsuario){
        return ResponseEntity.ok(service.getByUsuario(idUsuario));
    }

    @GetMapping("/{id}/recomendacion")
    public ResponseEntity<String>getRecomendacion(@PathVariable Long id){
        return ResponseEntity.ok(service.getRecomendacion(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KitEmergenciDTO> update(@PathVariable Long id, @RequestBody KitEmergenciDTO dto){
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
