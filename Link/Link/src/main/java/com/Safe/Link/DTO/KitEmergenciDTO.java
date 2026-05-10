package com.Safe.Link.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Data
public class KitEmergenciDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private Long idUsuario;
    private LocalDate fechaUltimaRevision;
    private Double porcentajeCompletado;
}
