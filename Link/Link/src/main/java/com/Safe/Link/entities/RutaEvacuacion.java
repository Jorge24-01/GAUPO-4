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
@Table(name = "ruta_evacuacion")
public class RutaEvacuacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Long id;
    @Column(name = "nombre_ruta", length = 150)
    private String nombreRuta;
    @Column(name = "descripcion", length = 200)
    private String descripcion;
    @Column(name = "distancia_km")
    private Double distanciakm;
    @ManyToOne
    @JoinColumn(name = "punto_destino", nullable = false)
    private PuntoSeguro puntoDestino;
}
