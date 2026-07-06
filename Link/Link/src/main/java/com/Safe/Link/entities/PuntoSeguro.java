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
@Table(name = "Punto_seguro")
public class PuntoSeguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_punto")
    private Long id;
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;
    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;
    @Column(name = "latitud", nullable = false)
    private Double  latitud;
    @Column(name = "longitud", nullable = false)
    private Double longitud;
    @Column(name = "direccion",length = 200)
    private String direccion;
    @Column(name = "capacidad")
    private Integer capacidad;
    @Column(name = "es_oficial", nullable = false)
    private Boolean esOficial;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
