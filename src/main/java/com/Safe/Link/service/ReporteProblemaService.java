package com.Safe.Link.service;

import com.Safe.Link.DTO.ReporteProblemaDTO;
import com.Safe.Link.entities.ReporteProblema;
import com.Safe.Link.repositories.ReporteProblemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteProblemaService {

    private final ReporteProblemaRepository repository;

    public ReporteProblema crear(ReporteProblemaDTO dto) {
        if (dto.getDescripcion() == null || dto.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        if (dto.getDescripcion().length() > 500) {
            throw new IllegalArgumentException("La descripción no puede superar los 500 caracteres.");
        }
        ReporteProblema reporte = ReporteProblema.builder()
                .usuarioId(dto.getUsuarioId())
                .descripcion(dto.getDescripcion())
                .pantallaAfectada(dto.getPantallaAfectada())
                .build();
        return repository.save(reporte);
    }

    public List<ReporteProblema> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public List<ReporteProblema> listarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    public List<ReporteProblema> listarTodos() {
        return repository.findAll();
    }
}
