package com.Safe.Link.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
public class Miembros_de_familia_DTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)

    private Long id_mienbro;
    private Long id_familia;
    private String nombre;
    private int edad;
    private String parentezco;
    private int contacto;
}
