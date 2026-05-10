package com.Safe.Link.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refugio")
public class Refugio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_refugio")
    private Long id;
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;
    @Column(name = "direccion", nullable = false, length = 250)
    private String direccion;
    @Column(name = "latitud")
    private Double latitud;
    @Column(name = "longitud")
    private Double longitud;
    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;
    @Column(name = "ocupacion_actual", nullable = false)
    private Integer ocupacionActual;
    @Column(name = "disponible", nullable = false)
    private Boolean disponible = true;
    @Column(name = "contacto_encargado", nullable = false, length = 100)
    private String contactoEncargado;
}
