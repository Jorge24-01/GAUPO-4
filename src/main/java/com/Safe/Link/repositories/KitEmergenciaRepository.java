package com.Safe.Link.repositories;

import com.Safe.Link.entities.KitEmergencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KitEmergenciaRepository extends JpaRepository<KitEmergencia, Long> {
    @Query("SELECT k FROM KitEmergencia k WHERE k.usuario.id = :idUsuario")
    Optional<KitEmergencia>findByUsuarioId(@Param("idUsuario") Long idUsuario);
}
