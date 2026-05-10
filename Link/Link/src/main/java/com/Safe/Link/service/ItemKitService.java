package com.Safe.Link.service;

import com.Safe.Link.DTO.ItemKitDTO;
import com.Safe.Link.entities.ItemKit;
import com.Safe.Link.entities.KitEmergencia;
import com.Safe.Link.repositories.ItemKitRepository;
import com.Safe.Link.repositories.KitEmergenciaRepository;
import com.Safe.Link.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemKitService {
    private final ItemKitRepository repository;
    private final KitEmergenciaRepository kitRepository;

    public List<ItemKitDTO>getByKit(Long idKit){
        if (!kitRepository.existsById(idKit))
            throw new IllegalArgumentException("Kit no encontrado: " + idKit);
        return repository.findByKitId(idKit).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public ItemKitDTO create(ItemKitDTO dto){
        if (dto.getNombreItem()==null || dto.getNombreItem().isBlank())
            throw new IllegalArgumentException("El nomber del item es obligatorio");
        KitEmergencia kit = kitRepository.findById(dto.getIdKit())
                .orElseThrow(()->new IllegalArgumentException("Kit no encontrado"));
        ItemKit e = new ItemKit();
        e.setKit(kit); e.setNombreItem(dto.getNombreItem());
        e.setCategoria(dto.getCategoria());
        e.setCantidadRecomendada(dto.getCantidadRecomendada() !=null ? dto.getCantidadRecomendada() : 1);
        e.setTieneItem(false);
        return toDTO(repository.save(e));
    }

    public List<ItemKitDTO>getByCategoria(String categoria){
        List<String>validos=List.of("agua", "alimento","medicamento","documentos","herramienta","otros");
        if (!validos.contains(categoria))
            throw new IllegalArgumentException("Categoria invalida: " + categoria);
        return repository.findByCategoria(categoria).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }


    public ItemKitDTO update(Long id, ItemKitDTO dto){
        ItemKit i = repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Item no encontrado: " + id));
        i.setNombreItem(dto.getNombreItem());
        i.setCategoria(dto.getCategoria());
        i.setCantidadRecomendada(dto.getCantidadRecomendada());
        if (dto.getTieneItem() != null) {
            i.setTieneItem(dto.getTieneItem());
        }
        return toDTO(repository.save(i));
    }

    public void delete(Long id){
        if (!repository.existsById(id))
            throw new IllegalArgumentException("Item no encontrado: " + id);
        repository.deleteById(id);
    }

    private ItemKitDTO toDTO(ItemKit e){
        ItemKitDTO dto = new ItemKitDTO();
        dto.setId(e.getId()); dto.setIdKit(e.getKit().getId());
        dto.setNombreItem(e.getNombreItem()); dto.setCategoria(e.getCategoria());
        dto.setCantidadRecomendada(e.getCantidadRecomendada());
        dto.setTieneItem(e.getTieneItem());
        return dto;
    }
}
