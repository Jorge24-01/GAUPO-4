package com.Safe.Link.repositories;

import com.Safe.Link.entities.MarcadorPersonalizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarcadorPersonalizadoRepository extends JpaRepository<MarcadorPersonalizado, Long> {
    @Query("SELECT m FROM MarcadorPersonalizado m WHERE m.usuario.id = :idUsuario ORDER BY m.id DESC")
    List<MarcadorPersonalizado> findByUsuarioId(@Param("idUsuario") Long idUsuario);
}
