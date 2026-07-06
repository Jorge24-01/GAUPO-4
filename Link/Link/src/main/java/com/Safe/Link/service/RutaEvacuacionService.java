package com.Safe.Link.service;

import com.Safe.Link.DTO.RutaEvacuacionDTO;
import com.Safe.Link.entities.PuntoSeguro;
import com.Safe.Link.entities.RutaEvacuacion;
import com.Safe.Link.repositories.PuntoSeguroRepository;
import com.Safe.Link.repositories.RutaEvacuancionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RutaEvacuacionService {
    private final RutaEvacuancionRepository repository;
    private final PuntoSeguroRepository puntoRepo;

    public List<RutaEvacuacionDTO>getByPuntoDestino(Long idPunto){
        if (!puntoRepo.existsById(idPunto))
            throw new IllegalArgumentException("punto.seguro.no.encontrado");
        return repository.findByPuntoDestino(idPunto).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public RutaEvacuacionDTO create(RutaEvacuacionDTO dto){
        if (dto.getNombreRuta() == null || dto.getNombreRuta().isBlank())
            throw new IllegalArgumentException("ruta.nombre.obligatorio");
        if (dto.getIdPuntoDestino() == null)
            throw new IllegalArgumentException("ruta.destino.obligatorio");

        PuntoSeguro puntoDestino = puntoRepo.findById(dto.getIdPuntoDestino())
                .orElseThrow(()->new IllegalArgumentException("punto.seguro.no.encontrado"));

        RutaEvacuacion e = new RutaEvacuacion();
        e.setPuntoDestino(puntoDestino);
        e.setNombreRuta(dto.getNombreRuta()); e.setDistanciakm(dto.getDistanciaKm());
        e.setDescripcion(dto.getDescripcion());
        return toDTO(repository.save(e));
    }

    public RutaEvacuacionDTO update(Long id, RutaEvacuacionDTO dto){
        RutaEvacuacion e = repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("ruta.no.encontrada"));
        e.setNombreRuta(dto.getNombreRuta()); e.setDescripcion(dto.getDescripcion());
        e.setDistanciakm(dto.getDistanciaKm());
        return toDTO(repository.save(e));
    }

    public void delete(Long id){
        if (!repository.existsById(id))
            throw new IllegalArgumentException("ruta.no.encontrada");
        repository.deleteById(id);
    }


    private RutaEvacuacionDTO toDTO(RutaEvacuacion e){
        RutaEvacuacionDTO dto = new RutaEvacuacionDTO();
        dto.setId(e.getId());
        dto.setIdPuntoDestino(e.getPuntoDestino().getId());
        dto.setDistanciaKm(e.getDistanciakm());
        dto.setNombreRuta(e.getNombreRuta()); dto.setDescripcion(e.getDescripcion());
        return dto;
    }
}
