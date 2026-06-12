package com.Safe.Link.controller;

import com.Safe.Link.DTO.NotifiacionDTO;
import com.Safe.Link.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionesController {
    private final NotificacionService service;

    @GetMapping("/usuario/{idUsuario}/no-leidas")
    public ResponseEntity<List<NotifiacionDTO>>getNoLeidas(@PathVariable Long idUsuario){
        return ResponseEntity.ok(service.getNoLeidas(idUsuario));
    }

    @PostMapping
    public ResponseEntity<NotifiacionDTO>create(@RequestBody NotifiacionDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<NotifiacionDTO>marcarLeido(@PathVariable Long id){
        return ResponseEntity.ok(service.marcarLeido(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
