package com.Safe.Link.service;

import com.Safe.Link.DTO.KitEmergenciDTO;
import com.Safe.Link.entities.KitEmergencia;
import com.Safe.Link.entities.Usuario;
import com.Safe.Link.repositories.KitEmergenciaRepository;
import com.Safe.Link.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KitEmergenciaService {

    private final KitEmergenciaRepository repository;
    private final UsuarioRepository usuarioRepository;

    public KitEmergenciDTO getByUsuario(Long idUsuario){
        return toDTO(repository.findByUsuarioId(idUsuario)
                .orElseThrow(()->new IllegalArgumentException("No tienes un kit registrado")));
    }

    public KitEmergenciDTO create(KitEmergenciDTO dto){
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(()->new IllegalArgumentException("Usuario no encontrado"));
        if (repository.findByUsuarioId(dto.getIdUsuario()).isPresent())
            throw new IllegalArgumentException("El usuario ya tiene un kit registrado");
        KitEmergencia k = new KitEmergencia();
        k.setUsuario(usuario); k.setPorcentaje(0.0);
        return toDTO(repository.save(k));
    }

    public KitEmergenciDTO update(Long id, KitEmergenciDTO dto){
        KitEmergencia k = repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Kit no encontrado: " + id));
        k.setFechaUltimaRevision(dto.getFechaUltimaRevision());
        return toDTO(repository.save(k));
    }

    public void delete(Long id){
        if (!repository.existsById(id))
            throw new IllegalArgumentException("Kit no encontrado: " + id);
        repository.deleteById(id);
    }

    public String getRecomendacion(Long idKit){
        KitEmergencia kit = repository.findById(idKit)
                .orElseThrow(()->new IllegalArgumentException("Kit no encontrado: " + idKit));
        double porcentaje = kit.getPorcentaje() != null ? kit.getPorcentaje() : 0.0;
        if (porcentaje>=80)
            return "Tu kit esta bien preparado. Revisa las fechas de vencimiento de tus items.";
        else if (porcentaje>=50)
            return "Tu kit esta incompleto. Prioriza agua, medicamentos y documentos importantes";
        else
            return "Tu kit necesita atencion urgente.";
    }

    private KitEmergenciDTO toDTO(KitEmergencia e){
        KitEmergenciDTO dto = new KitEmergenciDTO();
        dto.setId(e.getId());
        dto.setIdUsuario(e.getUsuario().getId());
        dto.setFechaUltimaRevision(e.getFechaUltimaRevision());
        dto.setPorcentajeCompletado(e.getPorcentaje());
        return dto;
    }
}
