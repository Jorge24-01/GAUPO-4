package com.Safe.Link.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
public class RutaEvacuacionDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private Long idPuntoDestino;
    private String nombreRuta;
    private String descripcion;
    private Double distanciaKm;
}
