package com.Safe.Link.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteProblemaDTO {
    private Long usuarioId;
    private String descripcion;   // max 500 chars - validado en controller
    private String pantallaAfectada;
}
