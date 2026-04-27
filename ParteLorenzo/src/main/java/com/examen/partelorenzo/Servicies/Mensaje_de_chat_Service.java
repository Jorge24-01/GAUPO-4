package com.examen.partelorenzo.Servicies;

import com.examen.partelorenzo.entities.Mensaje_de_chat;
import com.examen.partelorenzo.repositories.Mensaje_de_chat_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Mensaje_de_chat_Service {
    @Autowired
    private Mensaje_de_chat_repository mensaje_de_chat_repository;
    public List<Mensaje_de_chat> obtenerHistoriaPorCanal(String idUsuario, String TipoCanal) {
    return mensaje_de_chat_repository.findAll().stream().
            filter(m-> m.getId_mensaje()
                    .equals(idUsuario) && m.getTipo_canal().equals(m.getTipo_canal())).toList();
    }

    public Mensaje_de_chat enviarMensaje(Mensaje_de_chat mensaje) {
       if (mensaje.getFecha_hora()==null){
           mensaje.setFecha_hora(LocalDateTime.now());
       }
       if(mensaje.getEnviado_offline()==null){
           mensaje.setEnviado_offline("NO");
       }
       return mensaje_de_chat_repository.save(mensaje);

    }

    public List<Mensaje_de_chat> buscarEnChat(String palabraClave){
        return mensaje_de_chat_repository.findAll().stream()
                .filter(m->m.getContenido().contains(palabraClave)).toList();

    }


}

