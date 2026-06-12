package com.Safe.Link.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "numeros_emergencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NumeroEmergencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre; // Bomberos, Policía, SAMU

    @Column(nullable = false, length = 20)
    private String numero; // 116, 105, 117

    @Column(length = 200)
    private String descripcion;

    @Column(length = 50)
    private String icono; // nombre del ícono

    @Column(name = "orden_visualizacion")
    private Integer ordenVisualizacion;
}
