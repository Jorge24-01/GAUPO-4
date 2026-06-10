package com.Safe.Link.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsejoPreventivoDTO {
    private Long id;
    private String contenido;
    private String tipoDesastre;
    private String categoria;
    private Integer ordenVisualizacion;
}
