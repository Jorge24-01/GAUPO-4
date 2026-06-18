package com.Safe.Link.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarcadorPersonalizadoDTO {
    private Long id;
    private Long idUsuario;
    private String nombre;
    private Double latitud;
    private Double longitud;
    private String notas;
}
