package com.Safe.Link.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqPreguntaDTO {
    private Long id;
    private String pregunta;
    private String respuesta;
    private String categoria;
    private Integer ordenRelevancia;
    private String fuenteOficial;
}
