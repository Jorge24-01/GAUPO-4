package com.Safe.Link.controller;

import com.Safe.Link.DTO.RefugioDTO;
import com.Safe.Link.service.RefugioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refugios")
@RequiredArgsConstructor
public class RefugioController {
    private final RefugioService service;

    @GetMapping("/disponible")
    public ResponseEntity<List<RefugioDTO>>getDisponibles(){
        return ResponseEntity.ok(service.getDisponible());
    }

    @GetMapping
    public ResponseEntity<List<RefugioDTO>>getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<RefugioDTO>create(@RequestBody RefugioDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}/ingreso")
    public ResponseEntity<RefugioDTO>registrarIngreso(@PathVariable Long id){
        return ResponseEntity.ok(service.registrarIngreso(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
