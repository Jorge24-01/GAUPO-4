package com.Safe.Link.controller;

import com.Safe.Link.DTO.RutaEvacuacionDTO;
import com.Safe.Link.service.RutaEvacuacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutas-evacuacion")
@RequiredArgsConstructor
public class RutaEvacuacionController {
    private final RutaEvacuacionService service;

    @GetMapping("/punto/{idPunto}")
    public ResponseEntity<List<RutaEvacuacionDTO>>getByPunto(@PathVariable Long idPunto){
        return ResponseEntity.ok(service.getByPuntoDestino(idPunto));
    }

    @PostMapping
    public ResponseEntity<RutaEvacuacionDTO>create(@RequestBody RutaEvacuacionDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RutaEvacuacionDTO>update(@PathVariable Long id, @RequestBody RutaEvacuacionDTO dto){
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}


