package com.Safe.Link.service;

import com.Safe.Link.DTO.MarcadorPersonalizadoDTO;
import com.Safe.Link.entities.MarcadorPersonalizado;
import com.Safe.Link.entities.Usuario;
import com.Safe.Link.repositories.MarcadorPersonalizadoRepository;
import com.Safe.Link.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarcadorPersonalizadoService {
    private final MarcadorPersonalizadoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public List<MarcadorPersonalizadoDTO>getByUsuario(Long idUsuario){
        if (!usuarioRepository.existsById(idUsuario))
            throw new IllegalArgumentException("usuario.no.encontrado");
        return repository.findByUsuarioId(idUsuario).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public MarcadorPersonalizadoDTO create(MarcadorPersonalizadoDTO dto){
        if (dto.getNombre() == null || dto.getNombre().isBlank())
            throw new IllegalArgumentException("marcador.nombre.obligatorio");
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(()->new IllegalArgumentException("usuario.no.encontrado"));
        MarcadorPersonalizado e = new MarcadorPersonalizado();
        e.setUsuario(usuario); e.setNombre(dto.getNombre());
        e.setLatitud(dto.getLatitud()); e.setLongitud(dto.getLongitud());
        e.setNotas(dto.getNotas());
        return toDTO(repository.save(e));
    }

    public MarcadorPersonalizadoDTO update(Long id, MarcadorPersonalizadoDTO dto){
        MarcadorPersonalizado m = repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("marcador.no.encontrado"));
        m.setNombre(dto.getNombre());
        m.setLatitud(dto.getLatitud());
        m.setLongitud(dto.getLongitud());
        m.setNotas(dto.getNotas());
        return toDTO(repository.save(m));
    }

    public void delete(Long id){
        if (!repository.existsById(id))
            throw new IllegalArgumentException("marcador.no.encontrado");
        repository.deleteById(id);
    }

    private MarcadorPersonalizadoDTO toDTO(MarcadorPersonalizado e){
        MarcadorPersonalizadoDTO dto = new MarcadorPersonalizadoDTO();
        dto.setId(e.getId()); dto.setIdUsuario(e.getUsuario().getId());
        dto.setNombre(e.getNombre()); dto.setLatitud(e.getLatitud());
        dto.setLongitud(e.getLongitud()); dto.setNotas(e.getNotas());
        return dto;
    }
}
