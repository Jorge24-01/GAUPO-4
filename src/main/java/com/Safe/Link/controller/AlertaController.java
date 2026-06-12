package com.Safe.Link.controller;

import com.Safe.Link.DTO.AlertaDTO;
import com.Safe.Link.service.AlertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
public class AlertaController {
    private final AlertaService service;

    @PostMapping
    public ResponseEntity<AlertaDTO> create(@RequestBody AlertaDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<AlertaDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<AlertaDTO>> getByUsuario(@PathVariable Long idUsuario){
        return ResponseEntity.ok(service.getByUsuario(idUsuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertaDTO> update(@PathVariable Long id, @RequestBody AlertaDTO dto){
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}