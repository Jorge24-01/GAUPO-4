package com.Safe.Link.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class IncidenteDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    private Long idUsuario;
    private String tipo;
    private String descripcion;
    private Double latitud;
    private Double longitud;
    private LocalDateTime fechaHora;
    private String estado;
}
