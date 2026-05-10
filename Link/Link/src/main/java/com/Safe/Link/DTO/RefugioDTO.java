package com.Safe.Link.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
public class RefugioDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String nombre;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private Integer capacidadMaxima;
    private Integer ocupacionActual;
    private Boolean disponible;
    private String contactoEncargado;
}
