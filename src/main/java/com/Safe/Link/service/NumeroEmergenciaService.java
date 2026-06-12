package com.Safe.Link.service;

import com.Safe.Link.DTO.NumeroEmergenciaDTO;
import com.Safe.Link.entities.NumeroEmergencia;
import com.Safe.Link.repositories.NumeroEmergenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NumeroEmergenciaService {

    private final NumeroEmergenciaRepository repository;

    public List<NumeroEmergenciaDTO> listarTodos() {
        return repository.findAllByOrderByOrdenVisualizacionAsc()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public NumeroEmergenciaDTO obtenerPorId(Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Número de emergencia no encontrado: " + id));
    }

    public NumeroEmergenciaDTO crear(NumeroEmergenciaDTO dto) {
        NumeroEmergencia entity = toEntity(dto);
        return toDTO(repository.save(entity));
    }

    public NumeroEmergenciaDTO actualizar(Long id, NumeroEmergenciaDTO dto) {
        NumeroEmergencia existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Número de emergencia no encontrado: " + id));
        existing.setNombre(dto.getNombre());
        existing.setNumero(dto.getNumero());
        existing.setDescripcion(dto.getDescripcion());
        existing.setIcono(dto.getIcono());
        existing.setOrdenVisualizacion(dto.getOrdenVisualizacion());
        return toDTO(repository.save(existing));
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    private NumeroEmergenciaDTO toDTO(NumeroEmergencia e) {
        return NumeroEmergenciaDTO.builder()
                .id(e.getId()).nombre(e.getNombre()).numero(e.getNumero())
                .descripcion(e.getDescripcion()).icono(e.getIcono())
                .ordenVisualizacion(e.getOrdenVisualizacion()).build();
    }

    private NumeroEmergencia toEntity(NumeroEmergenciaDTO dto) {
        return NumeroEmergencia.builder()
                .nombre(dto.getNombre()).numero(dto.getNumero())
                .descripcion(dto.getDescripcion()).icono(dto.getIcono())
                .ordenVisualizacion(dto.getOrdenVisualizacion()).build();
    }
}
