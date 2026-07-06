package com.Safe.Link.service;

import com.Safe.Link.DTO.FaqPreguntaDTO;
import com.Safe.Link.entities.FaqPregunta;
import com.Safe.Link.repositories.FaqPreguntaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaqPreguntaService {

    private final FaqPreguntaRepository repository;

    public List<FaqPreguntaDTO> listarTodas() {
        return repository.findAllByOrderByOrdenRelevanciaAsc()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<FaqPreguntaDTO> filtrarPorCategoria(String categoria) {
        return repository.findByCategoria(categoria)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public FaqPreguntaDTO obtenerPorId(Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("faq.no.encontrada"));
    }

    public FaqPreguntaDTO crear(FaqPreguntaDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    public FaqPreguntaDTO actualizar(Long id, FaqPreguntaDTO dto) {
        FaqPregunta e = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("faq.no.encontrada"));
        e.setPregunta(dto.getPregunta());
        e.setRespuesta(dto.getRespuesta());
        e.setCategoria(dto.getCategoria());
        e.setOrdenRelevancia(dto.getOrdenRelevancia());
        e.setFuenteOficial(dto.getFuenteOficial());
        return toDTO(repository.save(e));
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    private FaqPreguntaDTO toDTO(FaqPregunta e) {
        return FaqPreguntaDTO.builder()
                .id(e.getId()).pregunta(e.getPregunta()).respuesta(e.getRespuesta())
                .categoria(e.getCategoria()).ordenRelevancia(e.getOrdenRelevancia())
                .fuenteOficial(e.getFuenteOficial()).build();
    }

    private FaqPregunta toEntity(FaqPreguntaDTO dto) {
        return FaqPregunta.builder()
                .pregunta(dto.getPregunta()).respuesta(dto.getRespuesta())
                .categoria(dto.getCategoria()).ordenRelevancia(dto.getOrdenRelevancia())
                .fuenteOficial(dto.getFuenteOficial()).build();
    }
}
