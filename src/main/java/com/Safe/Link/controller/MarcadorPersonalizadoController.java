package com.Safe.Link.controller;

import com.Safe.Link.DTO.MarcadorPersonalizadoDTO;
import com.Safe.Link.service.MarcadorPersonalizadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcadores")
@RequiredArgsConstructor
public class MarcadorPersonalizadoController {
    private final MarcadorPersonalizadoService service;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<MarcadorPersonalizadoDTO>>getByUsuario(@PathVariable Long idUsuario){
        return ResponseEntity.ok(service.getByUsuario(idUsuario));
    }

    @PostMapping
    public ResponseEntity<MarcadorPersonalizadoDTO>create(@RequestBody MarcadorPersonalizadoDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarcadorPersonalizadoDTO> update(@PathVariable Long id, @RequestBody MarcadorPersonalizadoDTO dto){
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
