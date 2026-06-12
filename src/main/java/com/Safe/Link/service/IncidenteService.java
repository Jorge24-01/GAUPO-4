package com.Safe.Link.service;

import com.Safe.Link.DTO.IncidenteDTO;
import com.Safe.Link.entities.Incidente;
import com.Safe.Link.entities.Usuario;
import com.Safe.Link.repositories.IncidenteRepository;
import com.Safe.Link.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidenteService {
    private final IncidenteRepository repository;
    private final UsuarioRepository usuarioRepository;

    public IncidenteDTO create(IncidenteDTO dto){
        if (dto.getTipo() == null || dto.getTipo().isBlank())
            throw new IllegalArgumentException("El tipo de incidente es obligatorio");
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));
        Incidente e= new Incidente();
        e.setUsuario(usuario); e.setTipo(dto.getTipo());
        e.setDescripcion(dto.getDescripcion());
        e.setLatitud(dto.getLatitud()); e.setLongitud(dto.getLongitud());
        e.setFechaHora(LocalDateTime.now()); e.setEstado("reportado");
        return toDTO(repository.save(e));
    }

    public List<IncidenteDTO>getByEstado(String estado){
        List<String>validos= List.of("reportado", "verificado", "resuelto");
        if (!validos.contains(estado))
            throw new IllegalArgumentException("Estado invalido: " + estado);
        return repository.findByEstado(estado).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<IncidenteDTO>getByUsuario(Long idUsuario){
        return repository.findByUsuarioID(idUsuario).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<IncidenteDTO>getByTipo(String tipo){
        List<String>validos=List.of("sismo","inundacion","incendio","deslizamiento","otro");
        if (!validos.contains(tipo))
         throw new IllegalArgumentException("Tipo invalido : " + tipo);
        return repository.findByTipo(tipo).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public IncidenteDTO update(Long id, IncidenteDTO dto){
        Incidente i = repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Incidente no encontrado: " + id));
        i.setTipo(dto.getTipo());
        i.setDescripcion(dto.getDescripcion());
        i.setLatitud(dto.getLatitud());
        i.setLongitud(dto.getLongitud());
        i.setEstado(dto.getEstado());
        return toDTO(repository.save(i));
    }

    public void delete(Long id){
        if (!repository.existsById(id))
            throw new IllegalArgumentException("Incidente no encontrado: " + id);
        repository.deleteById(id);
    }

    private IncidenteDTO toDTO(Incidente e){
        IncidenteDTO dto = new IncidenteDTO();
        dto.setId(e.getId()); dto.setIdUsuario(e.getUsuario().getId());
        dto.setTipo(e.getTipo()); dto.setDescripcion(e.getDescripcion());
        dto.setLatitud(e.getLatitud()); dto.setLongitud(e.getLongitud());
        dto.setFechaHora(e.getFechaHora()); dto.setEstado(e.getEstado());
        return dto;
    }
}
