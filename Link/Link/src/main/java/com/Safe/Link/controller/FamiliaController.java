package com.Safe.Link.controller;

import com.Safe.Link.DTO.FamliaDTO;
import com.Safe.Link.service.FamiliaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/familias")
@RequiredArgsConstructor
public class FamiliaController {
    private final FamiliaService service;

    @GetMapping("/{id}")
    public ResponseEntity<FamliaDTO>getById(@PathVariable("id") Long idFamilia){
        return ResponseEntity.ok(service.getById(idFamilia));
    }

    @PostMapping
    public ResponseEntity<FamliaDTO>create(@RequestBody FamliaDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<FamliaDTO>getByUsuario(@PathVariable Long idUsuario){
        return ResponseEntity.ok(service.getByUsuario(idUsuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FamliaDTO> update(@PathVariable("id") Long idFamilia, @RequestBody FamliaDTO dto){
        return ResponseEntity.ok(service.update(idFamilia,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
