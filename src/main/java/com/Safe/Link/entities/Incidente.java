package com.Safe.Link.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "incidente")
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incidente")
    private Long id;
    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;
    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;
    @Column(name = "latitud", nullable = false)
    private Double latitud;
    @Column(name = "longitud",nullable = false)
    private Double longitud;
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora=LocalDateTime.now();
    @Column(name = "estado", nullable = false, length = 30)
    private String estado;
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}
