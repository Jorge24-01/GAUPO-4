package com.Safe.Link.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class NotifiacionDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private Long idUsuarioDestino;
    private Long idAlerta;
    private String tipo;
    private Boolean leido;
    private LocalDate fechaHora;
}
