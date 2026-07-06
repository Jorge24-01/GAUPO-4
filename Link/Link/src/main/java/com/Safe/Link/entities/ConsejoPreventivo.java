package com.Safe.Link.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consejos_preventivos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsejoPreventivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "contenido_en", columnDefinition = "TEXT")
    private String contenidoEn;

    @Column(name = "tipo_desastre", length = 50)
    private String tipoDesastre;

    @Column(length = 100)
    private String categoria;

    @Column(name = "orden_visualizacion")
    private Integer ordenVisualizacion;
}
