package com.Safe.Link.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
public class MarcadorPersonalizadoDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private Long idUsuario;
    private String nombre;
    private Double latitud;
    private Double longitud;
    private String notas;
}
