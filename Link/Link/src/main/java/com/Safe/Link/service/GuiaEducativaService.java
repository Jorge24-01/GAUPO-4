package com.Safe.Link.service;

import com.Safe.Link.DTO.GuiaEducativaDTO;
import com.Safe.Link.entities.GuiaEducativa;
import com.Safe.Link.repositories.GuiaEducativaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuiaEducativaService {

    private final GuiaEducativaRepository repository;

    public List<GuiaEducativaDTO> listarTodas() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<GuiaEducativaDTO> filtrarPorTipoDesastre(String tipoDesastre) {
        return repository.findByTipoDesastre(tipoDesastre)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<GuiaEducativaDTO> filtrarPorCategoria(String categoria) {
        return repository.findByCategoria(categoria)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<GuiaEducativaDTO> filtrarPorFase(String fase) {
        return repository.findByFaseTemporal(fase)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<GuiaEducativaDTO> filtrarPorTipoYFase(String tipo, String fase) {
        return repository.findByTipoDesastreAndFaseTemporal(tipo, fase)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<GuiaEducativaDTO> listarOffline() {
        return repository.findByDisponibleOffline(true)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public GuiaEducativaDTO obtenerPorId(Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("guia.no.encontrada"));
    }

    public GuiaEducativaDTO crear(GuiaEducativaDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    public GuiaEducativaDTO actualizar(Long id, GuiaEducativaDTO dto) {
        GuiaEducativa e = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("guia.no.encontrada"));
        e.setTitulo(dto.getTitulo());
        e.setContenido(dto.getContenido());
        e.setCategoria(dto.getCategoria());
        e.setTipoDesastre(dto.getTipoDesastre());
        e.setFaseTemporal(dto.getFaseTemporal());
        e.setUrlImagen(dto.getUrlImagen());
        e.setDisponibleOffline(dto.getDisponibleOffline());
        return toDTO(repository.save(e));
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    private GuiaEducativaDTO toDTO(GuiaEducativa e) {
        return GuiaEducativaDTO.builder()
                .id(e.getId()).titulo(e.getTitulo()).contenido(e.getContenido())
                .categoria(e.getCategoria()).tipoDesastre(e.getTipoDesastre())
                .faseTemporal(e.getFaseTemporal()).urlImagen(e.getUrlImagen())
                .disponibleOffline(e.getDisponibleOffline()).build();
    }

    private GuiaEducativa toEntity(GuiaEducativaDTO dto) {
        return GuiaEducativa.builder()
                .titulo(dto.getTitulo()).contenido(dto.getContenido())
                .categoria(dto.getCategoria()).tipoDesastre(dto.getTipoDesastre())
                .faseTemporal(dto.getFaseTemporal()).urlImagen(dto.getUrlImagen())
                .disponibleOffline(dto.getDisponibleOffline() != null ? dto.getDisponibleOffline() : true).build();
    }
}
