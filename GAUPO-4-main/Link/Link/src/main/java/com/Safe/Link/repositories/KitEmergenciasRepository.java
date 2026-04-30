package com.Safe.Link.repositories;

import com.Safe.Link.entities.KitEmergencias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KitEmergenciasRepository extends JpaRepository<KitEmergencias, Long> {

    List<KitEmergencias> findByUsuario_Id(Long id);
}
