package com.Safe.Link.service;

import com.Safe.Link.DTO.NotifiacionDTO;
import com.Safe.Link.entities.Notificaciones;
import com.Safe.Link.entities.Usuario;
import com.Safe.Link.repositories.Alerta_repository;
import com.Safe.Link.repositories.NotifiacionRepository;
import com.Safe.Link.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacionService {
    private final NotifiacionRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final Alerta_repository alertaRepository;

    public List<NotifiacionDTO> getNoLeidas(Long idUsuario){
        if (!usuarioRepository.existsById(idUsuario))
            throw new IllegalArgumentException("usuario.no.encontrado");
        return repository.findNoLeidasByUsuario(idUsuario).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public NotifiacionDTO create(NotifiacionDTO dto){
        Usuario usuarioDestino = usuarioRepository.findById(dto.getIdUsuarioDestino())
                .orElseThrow(() -> new IllegalArgumentException("usuario.no.encontrado"));

        List<String> tiposValidos = List.of(
                "sismo",
                "alerta_sismo",
                "alerta_inundacion",
                "alerta_incendio",
                "alerta_deslizamiento",
                "alerta_tsunami",
                "alerta_general",
                "familiar_en_peligro",
                "refugio_disponible",
                "kit_incompleto",
                "ruta_actualizada",
                "punto_encuentro_asignado",
                "incidente_reportado"
        );

        if (dto.getTipo() == null || !tiposValidos.contains(dto.getTipo()))
            throw new IllegalArgumentException("notificacion.tipo.invalido");

        Notificaciones n = new Notificaciones();
        n.setUsuarioDestino(usuarioDestino);
        n.setTipo(dto.getTipo());
        n.setLeido(dto.getLeido() != null ? dto.getLeido() : false);
        n.setFecha_hora(dto.getFechaHora() != null ? dto.getFechaHora() : LocalDate.now());

        if (dto.getIdAlerta() != null){
            n.setAlerta(alertaRepository.findById(dto.getIdAlerta())
                    .orElseThrow(()->new IllegalArgumentException("alerta.no.encontrada")));
        }

        return toDTO(repository.save(n));
    }

    public NotifiacionDTO marcarLeido(Long id){
        Notificaciones n = repository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("notificacion.no.encontrada"));
        if (n.getLeido())
            throw new IllegalArgumentException("notificacion.ya.leida");
        n.setLeido(true);
        return toDTO(repository.save(n));
    }

    public void delete(Long id){
        if (!repository.existsById(id))
            throw new IllegalArgumentException("notificacion.no.encontrada");
        repository.deleteById(id);
    }

    private NotifiacionDTO toDTO(Notificaciones e){
        NotifiacionDTO dto = new NotifiacionDTO();
        dto.setId(e.getId());
        dto.setIdUsuarioDestino(e.getUsuarioDestino().getId());
        dto.setTipo(e.getTipo()); dto.setLeido(e.getLeido());
        dto.setFechaHora(e.getFecha_hora());
        if (e.getAlerta() != null) dto.setIdAlerta(e.getAlerta().getId());
        return dto;
    }
}
