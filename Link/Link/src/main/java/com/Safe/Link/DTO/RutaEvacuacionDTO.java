package com.Safe.Link.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RutaEvacuacionDTO {
    private Long id;
    private Long idPuntoDestino;
    private String nombreRuta;
    private String descripcion;
    private Double distanciaKm;
    private Boolean disponibleOffline;
}
