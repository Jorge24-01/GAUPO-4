package com.Safe.Link.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncidenteDTO {
    private Long id;
    private Long idUsuario;
    private String tipo;
    private String descripcion;
    private Double latitud;
    private Double longitud;
    private LocalDateTime fechaHora;
    private String estado;
}
