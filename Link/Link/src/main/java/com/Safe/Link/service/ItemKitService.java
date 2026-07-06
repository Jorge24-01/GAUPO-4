package com.Safe.Link.service;

import com.Safe.Link.DTO.ItemKitDTO;
import com.Safe.Link.entities.ItemKit;
import com.Safe.Link.entities.KitEmergencia;
import com.Safe.Link.repositories.ItemKitRepository;
import com.Safe.Link.repositories.KitEmergenciaRepository;
import com.Safe.Link.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemKitService {
    private final ItemKitRepository repository;
    private final KitEmergenciaRepository kitRepository;

    public List<ItemKitDTO>getRecomendados(){
        boolean ingles = "en".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage());
        return List.of(
                recomendado(ingles ? "Bottled water" : "Agua embotellada", "agua", 3),
                recomendado(ingles ? "Flashlight" : "Linterna", "herramienta", 1),
                recomendado(ingles ? "Portable radio" : "Radio portatil", "comunicacion", 1),
                recomendado(ingles ? "Basic first aid kit" : "Botiquin basico", "medicamento", 1),
                recomendado(ingles ? "Whistle" : "Silbato", "herramienta", 1),
                recomendado(ingles ? "Non-perishable food" : "Alimentos no perecibles", "alimentos", 5),
                recomendado(ingles ? "Copy of documents" : "Copia de documentos", "documentos", 1),
                recomendado(ingles ? "Thermal blanket" : "Manta termica", "abrigo", 1),
                recomendado(ingles ? "Portable charger" : "Cargador portatil", "comunicacion", 1),
                recomendado(ingles ? "Face masks" : "Mascarillas", "proteccion", 5)
        );
    }

    public List<ItemKitDTO>getByKit(Long idKit){
        if (!kitRepository.existsById(idKit))
            throw new IllegalArgumentException("kit.no.encontrado");
        return repository.findByKitId(idKit).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public ItemKitDTO create(ItemKitDTO dto){
        if (dto.getNombreItem()==null || dto.getNombreItem().isBlank())
            throw new IllegalArgumentException("item.nombre.obligatorio");
        KitEmergencia kit = kitRepository.findById(dto.getIdKit())
                .orElseThrow(()->new IllegalArgumentException("kit.no.encontrado"));
        ItemKit e = new ItemKit();
        e.setKit(kit); e.setNombreItem(dto.getNombreItem()); e.setNombreItemEn(dto.getNombreItemEn());
        e.setCategoria(dto.getCategoria());
        e.setCantidadRecomendada(dto.getCantidadRecomendada() !=null ? dto.getCantidadRecomendada() : 1);
        e.setTieneItem(false);
        return toDTO(repository.save(e));
    }

    public List<ItemKitDTO>getByCategoria(String categoria){
        List<String>validos=List.of("agua", "alimento","medicamento","documentos","herramienta","otros");
        if (!validos.contains(categoria))
            throw new IllegalArgumentException("item.categoria.invalida");
        return repository.findByCategoria(categoria).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }


    public ItemKitDTO update(Long id, ItemKitDTO dto){
        ItemKit i = repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("item.no.encontrado"));
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
            throw new IllegalArgumentException("item.no.encontrado");
        repository.deleteById(id);
    }

    private ItemKitDTO toDTO(ItemKit e){
        boolean ingles = "en".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage());
        String nombreResuelto = (ingles && e.getNombreItemEn() != null && !e.getNombreItemEn().isBlank())
                ? e.getNombreItemEn()
                : e.getNombreItem();
        ItemKitDTO dto = new ItemKitDTO();
        dto.setId(e.getId()); dto.setIdKit(e.getKit().getId());
        dto.setNombreItem(nombreResuelto); dto.setNombreItemEn(e.getNombreItemEn()); dto.setCategoria(e.getCategoria());
        dto.setCantidadRecomendada(e.getCantidadRecomendada());
        dto.setTieneItem(e.getTieneItem());
        return dto;
    }

    private ItemKitDTO recomendado(String nombre, String categoria, Integer cantidad){
        ItemKitDTO dto = new ItemKitDTO();
        dto.setNombreItem(nombre);
        dto.setCategoria(categoria);
        dto.setCantidadRecomendada(cantidad);
        dto.setTieneItem(false);
        return dto;
    }
}
