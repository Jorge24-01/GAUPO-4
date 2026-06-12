package com.Safe.Link.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompartirGuiaDTO {
    private Long idGuia;
    private String titulo;
    private String textoCompartir;  // texto prearmado para WhatsApp
    private String urlPublica;      // URL directa a la guía (acceso sin login)
}