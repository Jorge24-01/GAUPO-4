package com.Safe.Link.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoluntarioDTO {
    private Long id;
    private Long idUsuario;
    private String especialidad;
    private Boolean disponible;
}
