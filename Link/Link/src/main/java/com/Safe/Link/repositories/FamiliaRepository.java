package com.Safe.Link.repositories;

import com.Safe.Link.entities.Familia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamiliaRepository extends JpaRepository<Familia, Long> {
    @Query("SELECT f FROM Familia f WHERE f.usuario.id = :idUsuario")
    Optional<Familia>findByUsuarioId(@Param("idUsuario") Long idUsuario);
}
