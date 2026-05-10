package com.Safe.Link.repositories;

import com.Safe.Link.entities.Incidente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Long> {
    @Query("SELECT i FROM Incidente i WHERE i.estado = :estado ORDER BY i.fechaHora DESC")
    List<Incidente>findByEstado(@Param("estado") String estado);

    @Query("SELECT i FROM Incidente i WHERE i.usuario.id = :idUsuario ORDER BY i.fechaHora DESC")
    List<Incidente>findByUsuarioID(@Param("idUsuario")Long idUsuario);

    @Query("SELECT i FROM Incidente i WHERE i.tipo = :tipo ORDER BY i.fechaHora DESC")
    List<Incidente>findByTipo(@Param("tipo")String tipo);
}
