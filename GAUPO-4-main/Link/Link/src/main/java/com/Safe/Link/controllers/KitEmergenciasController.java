package com.Safe.Link.controllers;

import com.Safe.Link.DTO.KitEmergenciasDTO;
import com.Safe.Link.services.KitEmergenciasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kit")
public class KitEmergenciasController {

    private final KitEmergenciasService kitEmergenciasService;

    public KitEmergenciasController(KitEmergenciasService kitEmergenciasService) {
        this.kitEmergenciasService = kitEmergenciasService;
    }

    @PostMapping("/insert")
    public ResponseEntity<KitEmergenciasDTO> insertar(@RequestBody KitEmergenciasDTO dto) {
        try {
            KitEmergenciasDTO guardado = kitEmergenciasService.crear(dto);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return new ResponseEntity<>(kitEmergenciasService.listar(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KitEmergenciasDTO> obtenerPorId(@PathVariable Long id) {
        try {
            KitEmergenciasDTO kit = kitEmergenciasService.obtenerPorId(id);
            return new ResponseEntity<>(kit, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}