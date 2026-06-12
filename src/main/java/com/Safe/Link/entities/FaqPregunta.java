package com.Safe.Link.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "faq_preguntas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqPregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String pregunta;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String respuesta;

    @Column(length = 100)
    private String categoria;

    @Column(name = "orden_relevancia")
    private Integer ordenRelevancia;

    @Column(name = "fuente_oficial", length = 200)
    private String fuenteOficial; // INDECI, CENEPRED, etc.
}
