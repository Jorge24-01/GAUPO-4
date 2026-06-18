package com.Safe.Link.repositories;

import com.Safe.Link.entities.PuntoSeguro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuntoSeguroRepository extends JpaRepository<PuntoSeguro, Long> {

    List<PuntoSeguro> findByUsuario_Id(Long idUsuario);
}
