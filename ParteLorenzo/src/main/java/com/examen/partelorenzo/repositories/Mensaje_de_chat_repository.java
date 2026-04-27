package com.examen.partelorenzo.repositories;

import com.examen.partelorenzo.entities.Mensaje_de_chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface Mensaje_de_chat_repository extends JpaRepository<Mensaje_de_chat, String> {
    List<Mensaje_de_chat> findByIdUsusarioOrderByFechaHoraDesc(String id_usuario);
}
