package com.Safe.Link.repositories;

import com.Safe.Link.entities.Notificaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifiacionRepository extends JpaRepository<Notificaciones,Long> {

    @Query("SELECT n FROM Notificaciones n WHERE n.usuarioDestino.id = :idUsuario AND " +
            "n.leido = FALSE ORDER BY n.fecha_hora DESC ")
    List<Notificaciones>findNoLeidasByUsuario(@Param("idUsuario")Long idUsuario);

    @Query("SELECT n FROM Notificaciones n WHERE n.usuarioDestino.id = :idUsuario ORDER BY " +
            "n.fecha_hora DESC ")
    List<Notificaciones>findAllByUsuario(@Param("idUsuario")Long idUsuario);
}
