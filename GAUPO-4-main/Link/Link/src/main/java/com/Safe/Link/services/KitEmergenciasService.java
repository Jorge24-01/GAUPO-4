package com.Safe.Link.services;

import com.Safe.Link.DTO.KitEmergenciasDTO;
import com.Safe.Link.entities.KitEmergencias;
import com.Safe.Link.entities.Usuario;
import com.Safe.Link.repositories.KitEmergenciasRepository;
import com.Safe.Link.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KitEmergenciasService {

    private final KitEmergenciasRepository kitEmergenciasRepository;
    private final UsuarioRepository usuarioRepository;

    public KitEmergenciasService(KitEmergenciasRepository kitEmergenciasRepository,
                                 UsuarioRepository usuarioRepository) {
        this.kitEmergenciasRepository = kitEmergenciasRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public KitEmergenciasDTO crear(KitEmergenciasDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        KitEmergencias kit = new KitEmergencias();
        kit.setUsuario(usuario);
        kit.setFechaUltimaRevision(dto.getFechaUltimaRevision());
        kit.setCompletado(dto.getCompletado());

        KitEmergencias guardado = kitEmergenciasRepository.save(kit);
        return KitEmergenciasDTO.fromEntity(guardado);
    }

    @Transactional(readOnly = true)
    public List<KitEmergenciasDTO> listar() {
        return kitEmergenciasRepository.findAll()
                .stream()
                .map(KitEmergenciasDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public KitEmergenciasDTO obtenerPorId(Long id) {
        KitEmergencias kit = kitEmergenciasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kit no encontrado"));

        return KitEmergenciasDTO.fromEntity(kit);
    }
}