package com.Safe.Link.service;

import com.Safe.Link.DTO.PuntoSeguroDTO;
import com.Safe.Link.entities.PuntoSeguro;
import com.Safe.Link.repositories.PuntoSeguroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PuntoSeguroService {
    private final PuntoSeguroRepository repository;

    public List<PuntoSeguroDTO>getAll(){
        return repository.findAllOrdenados().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public PuntoSeguroDTO getById(Long id){
        return toDTO(repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Punto seguro no encontrado: " + id)));
    }

    public PuntoSeguroDTO create(PuntoSeguroDTO dto){
        if (dto.getNombre() == null || dto.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (dto.getLatitud() == null || dto.getLongitud() == null)
            throw new IllegalArgumentException("Las coordenadas son obligatorias");
        return toDTO(repository.save(toEntity(dto)));
    }

    public List<PuntoSeguroDTO>getBytipo(String tipo){
        List<String>valido=List.of("refugio", "punto de encuentro","zona segura");
        if (!valido.contains(tipo))
            throw new IllegalArgumentException("Tipo invalido");
        return repository.findByTipo(tipo).stream()
                .map(this::toDTO).collect(Collectors.toList());

    }


    public PuntoSeguroDTO update(Long id, PuntoSeguroDTO dto){
        PuntoSeguro u = repository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Punto seguro no encontrado"));
        u.setNombre(dto.getNombre());
        u.setTipo(dto.getTipo()); u.setLatitud(dto.getLatitud());
        u.setLongitud(dto.getLongitud()); u.setDireccion(dto.getDireccion());
        return toDTO(repository.save(u));
    }

    public void delete(Long id){
        if (!repository.existsById(id))
            throw new IllegalArgumentException("Punto seguro no encontrado: " + id);
        repository.deleteById(id);
    }


    private PuntoSeguroDTO toDTO(PuntoSeguro e){
        PuntoSeguroDTO dto = new PuntoSeguroDTO();
        dto.setId(e.getId()); dto.setNombre(e.getNombre());
        dto.setTipo(e.getTipo()); dto.setLatitud(e.getLatitud());
        dto.setLongitud(e.getLongitud()); dto.setDireccion(e.getDireccion());
        dto.setCapacidad(e.getCapacidad()); dto.setEsOficial(e.getEsOficial());
        return dto;
    }

    private PuntoSeguro toEntity(PuntoSeguroDTO dto){
        PuntoSeguro e = new PuntoSeguro();
        e.setNombre(dto.getNombre()); e.setTipo(dto.getTipo());
        e.setLatitud(dto.getLatitud()); e.setLongitud(dto.getLongitud());
        e.setDireccion(dto.getDireccion()); e.setCapacidad(dto.getCapacidad());
        e.setEsOficial(dto.getEsOficial() != null ? dto.getEsOficial(): true);
        return e;
    }

}
