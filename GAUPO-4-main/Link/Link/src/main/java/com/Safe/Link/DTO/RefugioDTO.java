package com.Safe.Link.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefugioDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private Integer capacidadMaxima;
    private Integer ocupacionActual;
    private Boolean disponible;
    private String contactoEncargado;
}
