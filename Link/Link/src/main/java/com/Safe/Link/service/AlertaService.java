package com.Safe.Link.service;

import com.Safe.Link.DTO.AlertaDTO;
import com.Safe.Link.entities.Alerta;
import com.Safe.Link.entities.Usuario;
import com.Safe.Link.repositories.Alerta_repository;
import com.Safe.Link.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertaService {
    private final Alerta_repository repository;
    private final UsuarioRepository usuarioRepository;


    public AlertaDTO create(AlertaDTO dto){
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(()->new IllegalArgumentException("Usuario no encontrado: " + dto.getIdUsuario()));
        if (dto.getTipoAlerta() == null || dto.getTipoAlerta().isBlank())
            throw new IllegalArgumentException("El tipo de alerta es obligatorio");
        if (dto.getLatitud() == null || dto.getLongitud() == null)
            throw new IllegalArgumentException("Las coordenadas son obligatorias");
        Alerta a = new Alerta();
        a.setUsuario(usuario);
        a.setTipoAlerta(dto.getTipoAlerta());
        a.setMensaje(dto.getMensaje());
        a.setLatitud(dto.getLatitud());
        a.setLongitud(dto.getLongitud());
        a.setFecha_hora(LocalDateTime.now());
        a.setEstado("activa");
        return toDTO(repository.save(a));
    }

    public List<AlertaDTO> getAll(){
        return repository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public AlertaDTO getById(Long id){
        return toDTO(repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Alerta no encontrada: " + id)));
    }

    public List<AlertaDTO> getByUsuario(Long idUsuario){
        if (!usuarioRepository.existsById(idUsuario))
            throw new IllegalArgumentException("Usuario no encontrado: " + idUsuario);
        return repository.findAll().stream()
                .filter(a -> a.getUsuario().getId().equals(idUsuario))
                .map(this::toDTO).collect(Collectors.toList());
    }

    public AlertaDTO update(Long id, AlertaDTO dto){
        Alerta a = repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Alerta no encontrada: " + id));
        a.setTipoAlerta(dto.getTipoAlerta());
        a.setMensaje(dto.getMensaje());
        a.setLatitud(dto.getLatitud());
        a.setLongitud(dto.getLongitud());
        a.setEstado(dto.getEstado());
        return toDTO(repository.save(a));
    }

    public void delete(Long id){
        if (!repository.existsById(id))
            throw new IllegalArgumentException("Alerta no encontrada: " + id);
        repository.deleteById(id);
    }

    private AlertaDTO toDTO(Alerta e){
        AlertaDTO dto = new AlertaDTO();
        dto.setId(e.getId());
        dto.setIdUsuario(e.getUsuario().getId());
        dto.setTipoAlerta(e.getTipoAlerta());
        dto.setMensaje(e.getMensaje());
        dto.setLatitud(e.getLatitud());
        dto.setLongitud(e.getLongitud());
        dto.setFechaHora(e.getFecha_hora());
        dto.setEstado(e.getEstado());
        return dto;
    }
}
