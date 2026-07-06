package com.Safe.Link.controller;

import com.Safe.Link.DTO.AsistenteRequestDTO;
import com.Safe.Link.DTO.AsistenteResponseDTO;
import com.Safe.Link.service.AsistenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/asistente")
@RequiredArgsConstructor
public class AsistenteController {

    private final AsistenteService asistenteService;

    @PostMapping("/preguntar")
    public ResponseEntity<AsistenteResponseDTO> preguntar(@RequestBody AsistenteRequestDTO request) {
        return ResponseEntity.ok(asistenteService.preguntar(request));
    }
}
