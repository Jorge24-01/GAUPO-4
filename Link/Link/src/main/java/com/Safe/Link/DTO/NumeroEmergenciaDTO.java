package com.Safe.Link.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NumeroEmergenciaDTO {
    private Long id;
    private String nombre;
    private String numero;
    private String descripcion;
    private String icono;
    private Integer ordenVisualizacion;
}
