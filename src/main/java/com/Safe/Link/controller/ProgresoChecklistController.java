package com.Safe.Link.controller;

import com.Safe.Link.DTO.ProgresoChecklistDTO;
import com.Safe.Link.service.ProgresoChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.Safe.Link.service.PdfKitService;

import java.util.List;

@RestController
@RequestMapping("/api/soporte/checklist")
@RequiredArgsConstructor
public class ProgresoChecklistController {
    private final ProgresoChecklistService service;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ProgresoChecklistDTO>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/completados")
    public ResponseEntity<List<ProgresoChecklistDTO>> obtenerCompletados(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.obtenerCompletados(usuarioId));
    }

    @PostMapping("/marcar")
    public ResponseEntity<ProgresoChecklistDTO> marcarItem(@RequestBody ProgresoChecklistDTO dto) {
        return ResponseEntity.ok(service.marcarItem(dto));
    }

    @PostMapping("/personalizado")
    public ResponseEntity<ProgresoChecklistDTO> agregarPersonalizado(@RequestBody ProgresoChecklistDTO dto) {
        return ResponseEntity.ok(service.agregarItemPersonalizado(dto));
    }

    @DeleteMapping("/usuario/{usuarioId}/item")
    public ResponseEntity<Void> eliminarItem(
            @PathVariable Long usuarioId,
            @RequestParam String nombreItem) {
        service.eliminarItemPersonalizado(usuarioId, nombreItem);
        return ResponseEntity.noContent().build();
    }
    private final PdfKitService pdfKitService;

    // US-30 — Escenarios 1, 2 y 3: descarga del PDF del kit personalizado
    @GetMapping("/usuario/{usuarioId}/pdf")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long usuarioId) {
        byte[] pdf = pdfKitService.generarPdfKit(usuarioId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Escenario 1: el navegador/móvil dispara la descarga directamente
        headers.setContentDispositionFormData("attachment", "kit-emergencia-safelink.pdf");
        headers.setContentLength(pdf.length);

        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
