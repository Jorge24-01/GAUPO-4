package com.Safe.Link.repositories;

import com.Safe.Link.entities.RutaEvacuacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaEvacuancionRepository extends JpaRepository<RutaEvacuacion, Long> {
    @Query("SELECT r FROM RutaEvacuacion r WHERE r.puntoDestino.id = :idPunto ORDER BY r.distanciakm ASC ")
    List<RutaEvacuacion>findByPuntoDestino(@Param("idPunto") Long idPunto);
}
