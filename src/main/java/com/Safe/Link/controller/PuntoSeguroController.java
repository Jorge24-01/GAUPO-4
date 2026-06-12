package com.Safe.Link.controller;

import com.Safe.Link.DTO.PuntoSeguroDTO;
import com.Safe.Link.service.PuntoSeguroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puntos-seguros")
@RequiredArgsConstructor
public class PuntoSeguroController {
    private final PuntoSeguroService service;

    @GetMapping
    public ResponseEntity<List<PuntoSeguroDTO>>getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<PuntoSeguroDTO> create(@RequestBody PuntoSeguroDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<PuntoSeguroDTO>>getByTipo(@PathVariable String tipo){
        return ResponseEntity.ok(service.getBytipo(tipo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuntoSeguroDTO>getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<PuntoSeguroDTO>update(@PathVariable Long id, @RequestBody PuntoSeguroDTO dto){
        return ResponseEntity.ok(service.update(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
