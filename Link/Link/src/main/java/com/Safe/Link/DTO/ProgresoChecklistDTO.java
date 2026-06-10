package com.Safe.Link.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgresoChecklistDTO {
    private Long id;
    private Long usuarioId;
    private String nombreItem;
    private Boolean esPersonalizado;
    private Boolean completado;
    private String categoria;
}
