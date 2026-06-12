package com.Safe.Link.controller;

import com.Safe.Link.DTO.LoginResponseDTO;
import com.Safe.Link.DTO.SesionDTO;
import com.Safe.Link.DTO.UsuarioDTO;
import com.Safe.Link.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO>create(@RequestBody UsuarioDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO>login(@RequestBody SesionDTO dto){
        return ResponseEntity.ok(service.login(dto));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>>getAll(){
        return ResponseEntity.ok(service.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO>getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<UsuarioDTO>>getByTipo(@PathVariable String tipo){
        return ResponseEntity.ok(service.getByTipo(tipo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO>update(@PathVariable Long id, @RequestBody UsuarioDTO dto){
        return ResponseEntity.ok(service.update(id,dto));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
