package com.Safe.Link.service;

import com.Safe.Link.DTO.FamliaDTO;
import com.Safe.Link.entities.Familia;
import com.Safe.Link.entities.Usuario;
import com.Safe.Link.repositories.FamiliaRepository;
import com.Safe.Link.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FamiliaService {
    private final FamiliaRepository repository;
    private final UsuarioRepository usuarioRepository;

    public FamliaDTO create(FamliaDTO dto){
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(()->new IllegalArgumentException("Usuario no encontrado: "));
        if (repository.findByUsuarioId(dto.getIdUsuario()).isPresent())
            throw new IllegalArgumentException("El usuario ya tiene una familia registrada");
        Familia f = new Familia();
        f.setUsuario(usuario); f.setNombreFamilia(dto.getNombreFamilia());
        return toDTO(repository.save(f));
    }

    public FamliaDTO getByUsuario(Long idUsuario){
        return toDTO(repository.findByUsuarioId(idUsuario)
                .orElseThrow(()->new IllegalArgumentException("No tienes una familia registrada")));
    }

    public FamliaDTO getById(Long id){
        Familia f = repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Familia no encontrada: " + id));
        return toDTO(f);
    }

    public FamliaDTO update(Long id, FamliaDTO dto){
        Familia f  = repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Familia no encontrada: " + id));

        f.setPuntoEncuentro(dto.getPuntoEncuentro());
        f.setNombreFamilia(dto.getNombreFamilia());
        return toDTO(repository.save(f));
    }


    public void delete(Long id){
        if (!repository.existsById(id))
            throw new IllegalArgumentException("Familia no encontrada: " + id);
        repository.deleteById(id);
    }



    private FamliaDTO toDTO(Familia e){
        FamliaDTO dto = new FamliaDTO();
        dto.setId(e.getId());
        dto.setIdUsuario(e.getUsuario().getId());
        dto.setNombreFamilia(e.getNombreFamilia());
        dto.setPuntoEncuentro(e.getPuntoEncuentro());
        return dto;
    }
}
