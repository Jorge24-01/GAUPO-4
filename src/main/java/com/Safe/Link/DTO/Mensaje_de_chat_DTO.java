package com.Safe.Link.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class Mensaje_de_chat_DTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)

    private Long id_mensaje;
    private Long id_Usuario;
    private String Contenido;
    private LocalDateTime fecha_hora;
    private String enviado_offline;
    private String tipo_canal;
}
