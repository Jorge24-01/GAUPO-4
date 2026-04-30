package com.Safe.Link.services;

import com.Safe.Link.entities.ItemDelKit;
import com.Safe.Link.repositories.ItemDelKitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemDelKitService {

    private final ItemDelKitRepository itemDelKitRepository;

    public ItemDelKitService(ItemDelKitRepository itemDelKitRepository) {
        this.itemDelKitRepository = itemDelKitRepository;
    }

    public ItemDelKit guardar(ItemDelKit itemDelKit) {
        return itemDelKitRepository.save(itemDelKit);
    }

    public List<ItemDelKit> listar() {
        return itemDelKitRepository.findAll();
    }

    public ItemDelKit buscarPorId(Long id) {
        return itemDelKitRepository.findById(id).orElse(null);
    }

    public List<ItemDelKit> listarPorKit(Long idKit) {
        return itemDelKitRepository.findByKitEmergencias_IdKit(idKit);
    }

    public void eliminar(Long id) {
        itemDelKitRepository.deleteById(id);
    }
}
