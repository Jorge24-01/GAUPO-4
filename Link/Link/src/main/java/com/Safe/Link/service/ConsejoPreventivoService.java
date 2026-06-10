package com.Safe.Link.service;

import com.Safe.Link.DTO.ConsejoPreventivoDTO;
import com.Safe.Link.entities.ConsejoPreventivo;
import com.Safe.Link.repositories.ConsejoPreventivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsejoPreventivoService {

    private final ConsejoPreventivoRepository repository;

    public List<ConsejoPreventivoDTO> listarTodos() {
        return repository.findAllByOrderByOrdenVisualizacionAsc()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ConsejoPreventivoDTO> filtrarPorTipo(String tipo) {
        return repository.findByTipoDesastre(tipo)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ConsejoPreventivoDTO crear(ConsejoPreventivoDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    private ConsejoPreventivoDTO toDTO(ConsejoPreventivo e) {
        return ConsejoPreventivoDTO.builder()
                .id(e.getId()).contenido(e.getContenido())
                .tipoDesastre(e.getTipoDesastre()).categoria(e.getCategoria())
                .ordenVisualizacion(e.getOrdenVisualizacion()).build();
    }

    private ConsejoPreventivo toEntity(ConsejoPreventivoDTO dto) {
        return ConsejoPreventivo.builder()
                .contenido(dto.getContenido()).tipoDesastre(dto.getTipoDesastre())
                .categoria(dto.getCategoria()).ordenVisualizacion(dto.getOrdenVisualizacion()).build();
    }
}
