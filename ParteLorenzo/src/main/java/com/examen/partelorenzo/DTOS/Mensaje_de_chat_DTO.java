package com.examen.partelorenzo.DTOS;


import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Mensaje_de_chat_DTO {

    private String id_mensaje;

    private String id_Usuario;

    private String Contenido;

    private LocalDateTime fecha_hora;

    private String enviado_offline;

    private String tipo_canal;

}
