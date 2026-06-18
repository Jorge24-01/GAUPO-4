package com.Safe.Link.services;

import com.Safe.Link.entities.PuntoSeguro;
import com.Safe.Link.repositories.PuntoSeguroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuntoSeguroService {

    private final PuntoSeguroRepository puntoSeguroRepository;

    public PuntoSeguroService(PuntoSeguroRepository puntoSeguroRepository) {
        this.puntoSeguroRepository = puntoSeguroRepository;
    }

    public PuntoSeguro guardar(PuntoSeguro puntoSeguro) {
        return puntoSeguroRepository.save(puntoSeguro);
    }

    public List<PuntoSeguro> listar() {
        return puntoSeguroRepository.findAll();
    }

    public PuntoSeguro buscarPorId(Long id) {
        return puntoSeguroRepository.findById(id).orElse(null);
    }

    public List<PuntoSeguro> listarPorUsuario(Long idUsuario) {
        return puntoSeguroRepository.findByUsuario_Id(idUsuario);
    }

    public void eliminar(Long id) {
        puntoSeguroRepository.deleteById(id);
    }
}
