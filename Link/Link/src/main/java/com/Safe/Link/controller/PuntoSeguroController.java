package com.Safe.Link.controller;

import com.Safe.Link.DTO.PuntoSeguroDTO;
import com.Safe.Link.service.PuntoSeguroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/usuario/{idUsuario}/mapa")
    public ResponseEntity<List<PuntoSeguroDTO>> getMapaUsuario(@PathVariable Long idUsuario,
                                                               Authentication authentication){
        return ResponseEntity.ok(service.getMapaUsuario(idUsuario, authentication.getName()));
    }

    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<PuntoSeguroDTO> createUsuario(@PathVariable Long idUsuario,
                                                        @RequestBody PuntoSeguroDTO dto,
                                                        Authentication authentication){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createUsuario(idUsuario, dto, authentication.getName()));
    }

    @DeleteMapping("/usuario/{idUsuario}/{idPunto}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long idUsuario,
                                              @PathVariable Long idPunto,
                                              Authentication authentication){
        service.deleteUsuario(idUsuario, idPunto, authentication.getName());
        return ResponseEntity.noContent().build();
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
