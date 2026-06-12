package com.Safe.Link.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuiaEducativaDTO {
    private Long id;
    private String titulo;
    private String contenido;
    private String categoria;
    private String tipoDesastre;
    private String faseTemporal;
    private String urlImagen;
    private Boolean disponibleOffline;
}
