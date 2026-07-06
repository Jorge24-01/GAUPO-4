package com.Safe.Link.service;

import com.Safe.Link.DTO.FamliaDTO;
import com.Safe.Link.DTO.Miembros_de_familia_DTO;
import com.Safe.Link.entities.Familia;
import com.Safe.Link.entities.Miembros_de_familia;
import com.Safe.Link.entities.Usuario;
import com.Safe.Link.repositories.FamiliaRepository;
import com.Safe.Link.repositories.Miembros_de_familia_repository;
import com.Safe.Link.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FamiliaService {
    private final FamiliaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final Miembros_de_familia_repository miembroRepository;

    public FamliaDTO create(FamliaDTO dto){
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(()->new IllegalArgumentException("usuario.no.encontrado"));
        if (repository.findByUsuarioId(dto.getIdUsuario()).isPresent())
            throw new IllegalArgumentException("familia.duplicada");
        Familia f = new Familia();
        f.setUsuario(usuario); f.setNombreFamilia(dto.getNombreFamilia());
        return toDTO(repository.save(f));
    }

    public FamliaDTO getByUsuario(Long idUsuario){
        return toDTO(repository.findByUsuarioId(idUsuario)
                .orElseThrow(()->new IllegalArgumentException("familia.no.registrada")));
    }

    public Miembros_de_familia_DTO registrarFamiliar(Long idUsuario, Miembros_de_familia_DTO dto){
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(()->new IllegalArgumentException("usuario.no.encontrado"));

        Familia familia = repository.findByUsuarioId(idUsuario)
                .orElseGet(() -> crearFamiliaBasica(usuario, dto));

        Miembros_de_familia familiar = new Miembros_de_familia();
        familiar.setId_familia(familia.getId());
        familiar.setNombre(dto.getNombre());
        familiar.setEdad(dto.getEdad());
        familiar.setParentezco(getRelacion(dto));
        familiar.setContacto(getContacto(dto));

        return toMiembroDTO(miembroRepository.save(familiar));
    }

    public List<FamliaDTO> getAll(){
        return repository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public FamliaDTO getById(Long id){
        Familia f = repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("familia.no.encontrada"));
        return toDTO(f);
    }

    public FamliaDTO update(Long id, FamliaDTO dto){
        Familia f  = repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("familia.no.encontrada"));

        f.setPuntoEncuentro(dto.getPuntoEncuentro());
        f.setNombreFamilia(dto.getNombreFamilia());
        return toDTO(repository.save(f));
    }


    public void delete(Long id){
        if (!repository.existsById(id))
            throw new IllegalArgumentException("familia.no.encontrada");
        repository.deleteById(id);
    }



    private FamliaDTO toDTO(Familia e){
        FamliaDTO dto = new FamliaDTO();
        dto.setId(e.getId());
        dto.setIdUsuario(e.getUsuario().getId());
        dto.setNombreFamilia(e.getNombreFamilia());
        dto.setPuntoEncuentro(e.getPuntoEncuentro());
        dto.setFamiliares(miembroRepository.findByIdFamilia(e.getId()).stream()
                .map(this::toMiembroDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private Familia crearFamiliaBasica(Usuario usuario, Miembros_de_familia_DTO dto){
        Familia familia = new Familia();
        familia.setUsuario(usuario);
        familia.setNombreFamilia("Familia de " + usuario.getNombre());
        familia.setPuntoEncuentro(dto.getUbicacionHabitual());
        return repository.save(familia);
    }

    private Miembros_de_familia_DTO toMiembroDTO(Miembros_de_familia e){
        Miembros_de_familia_DTO dto = new Miembros_de_familia_DTO();
        dto.setId(e.getId_miembro());
        dto.setId_mienbro(e.getId_miembro());
        dto.setId_familia(e.getId_familia());
        dto.setNombre(e.getNombre());
        dto.setEdad(e.getEdad());
        dto.setParentezco(e.getParentezco());
        dto.setRelacion(e.getParentezco());
        dto.setContacto(e.getContacto());
        dto.setTelefono(String.valueOf(e.getContacto()));
        return dto;
    }

    private String getRelacion(Miembros_de_familia_DTO dto){
        if (dto.getRelacion() != null && !dto.getRelacion().isBlank()) {
            return dto.getRelacion();
        }
        if (dto.getParentezco() != null && !dto.getParentezco().isBlank()) {
            return dto.getParentezco();
        }
        return "Familiar";
    }

    private int getContacto(Miembros_de_familia_DTO dto){
        if (dto.getTelefono() != null && !dto.getTelefono().isBlank()) {
            try {
                return Integer.parseInt(dto.getTelefono().replaceAll("\\D", ""));
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }
        return dto.getContacto();
    }
}
