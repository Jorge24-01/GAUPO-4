package com.Safe.Link.repositories;

import com.Safe.Link.entities.Mensaje_de_chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Mensaje_de_chat_repository extends JpaRepository<Mensaje_de_chat, Long> {

    @Query("SELECT m FROM Mensaje_de_chat m WHERE m.id_Usuario = :idUsuario ORDER BY m.fecha_hora DESC ")
    List<Mensaje_de_chat>findByUsuarioOrderByFechaHoraDesc(@Param("idUsuario") Long idUsuario);

    @Query("SELECT m FROM Mensaje_de_chat m WHERE m.tipo_canal = :canal ORDER BY m.fecha_hora DESC ")
    List<Mensaje_de_chat>findByTipoCanal(@Param("canal")String canal);
}
