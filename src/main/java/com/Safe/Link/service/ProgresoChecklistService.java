package com.Safe.Link.service;

import com.Safe.Link.DTO.ProgresoChecklistDTO;
import com.Safe.Link.entities.ProgresoChecklist;
import com.Safe.Link.repositories.ProgresoChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgresoChecklistService {

    private final ProgresoChecklistRepository repository;

    public List<ProgresoChecklistDTO> obtenerPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ProgresoChecklistDTO marcarItem(ProgresoChecklistDTO dto) {
        // Buscar si ya existe el item para ese usuario
        List<ProgresoChecklist> existentes = repository.findByUsuarioId(dto.getUsuarioId());
        ProgresoChecklist item = existentes.stream()
                .filter(p -> p.getNombreItem().equalsIgnoreCase(dto.getNombreItem()))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = toEntity(dto);
        } else {
            item.setCompletado(dto.getCompletado());
        }
        return toDTO(repository.save(item));
    }

    public ProgresoChecklistDTO agregarItemPersonalizado(ProgresoChecklistDTO dto) {
        dto.setEsPersonalizado(true);
        dto.setCompletado(false);
        return toDTO(repository.save(toEntity(dto)));
    }

    @Transactional
    public void eliminarItemPersonalizado(Long usuarioId, String nombreItem) {
        repository.deleteByUsuarioIdAndNombreItem(usuarioId, nombreItem);
    }

    public List<ProgresoChecklistDTO> obtenerCompletados(Long usuarioId) {
        return repository.findByUsuarioIdAndCompletado(usuarioId, true)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ProgresoChecklistDTO toDTO(ProgresoChecklist e) {
        return ProgresoChecklistDTO.builder()
                .id(e.getId()).usuarioId(e.getUsuarioId())
                .nombreItem(e.getNombreItem()).esPersonalizado(e.getEsPersonalizado())
                .completado(e.getCompletado()).categoria(e.getCategoria()).build();
    }

    private ProgresoChecklist toEntity(ProgresoChecklistDTO dto) {
        return ProgresoChecklist.builder()
                .usuarioId(dto.getUsuarioId()).nombreItem(dto.getNombreItem())
                .esPersonalizado(dto.getEsPersonalizado() != null ? dto.getEsPersonalizado() : false)
                .completado(dto.getCompletado() != null ? dto.getCompletado() : false)
                .categoria(dto.getCategoria()).build();
    }
}
