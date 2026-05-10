package com.Safe.Link.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotifiacionDTO {
    private Long id;
    private Long idUsuarioDestino;
    private Long idAlerta;
    private String tipo;
    private Boolean leido;
    private LocalDateTime fechaHora;
}
