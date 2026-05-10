package com.Safe.Link.repositories;

import com.Safe.Link.entities.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Alerta_repository extends JpaRepository<Alerta, Long> {
    @Query("SELECT a FROM Alerta a WHERE a.tipoAlerta = :tipo")
    List<Alerta>findByTipo(@Param("tipo")String tipo);
}
