package com.Safe.Link.service;

import com.Safe.Link.DTO.RefugioDTO;
import com.Safe.Link.entities.Refugio;
import com.Safe.Link.repositories.RefugioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefugioService {
    private final RefugioRepository repository;

    public List<RefugioDTO>getDisponible(){
        return repository.findDisponible().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<RefugioDTO>getAll(){
        return repository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public RefugioDTO create(RefugioDTO dto){
        if (dto.getNombre() == null || dto.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (dto.getCapacidadMaxima() == null || dto.getCapacidadMaxima() <=0)
            throw new IllegalArgumentException("La capacidad es obligatoria");
        Refugio r = new Refugio();

        r.setNombre(dto.getNombre());
        r.setDireccion(dto.getDireccion());
        r.setLatitud(dto.getLatitud());
        r.setLongitud(dto.getLongitud());
        r.setCapacidadMaxima(dto.getCapacidadMaxima());
        r.setOcupacionActual(0);
        r.setDisponible(true);
        r.setContactoEncargado(dto.getContactoEncargado());
        return toDTO(repository.save(r));
    }

    public RefugioDTO registrarIngreso(Long id){
        Refugio r = repository.findDisponibleById(id)
                .orElseThrow(()-> new IllegalArgumentException("Refugio no disponible"));
        if (r.getOcupacionActual() >= r.getCapacidadMaxima())
            throw new IllegalArgumentException("El refugio esta en su maxima capacidad");
        r.setOcupacionActual(r.getOcupacionActual() + 1);
        if (r.getOcupacionActual().equals(r.getCapacidadMaxima()))
            r.setDisponible(false);
        return toDTO(repository.save(r));
    }

    public void delete(Long id){
        if (!repository.existsById(id))
            throw new IllegalArgumentException("Refugio no encontrado: " + id);
        repository.deleteById(id);
    }

    private RefugioDTO toDTO(Refugio e){
        RefugioDTO dto = new RefugioDTO();
        dto.setId(e.getId()); dto.setNombre(e.getNombre());
        dto.setDireccion(e.getDireccion()); dto.setLatitud(e.getLatitud());
        dto.setLongitud(e.getLongitud()); dto.setCapacidadMaxima(e.getCapacidadMaxima());
        dto.setOcupacionActual(e.getOcupacionActual()); dto.setDisponible(e.getDisponible());
        dto.setContactoEncargado(e.getContactoEncargado());
        return dto;
    }
}
