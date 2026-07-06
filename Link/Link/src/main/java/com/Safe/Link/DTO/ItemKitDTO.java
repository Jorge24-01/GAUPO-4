package com.Safe.Link.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
public class ItemKitDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private Long idKit;
    private String nombreItem;
    private String nombreItemEn;
    private String categoria;
    private Integer cantidadRecomendada;
    private Boolean tieneItem;
}
