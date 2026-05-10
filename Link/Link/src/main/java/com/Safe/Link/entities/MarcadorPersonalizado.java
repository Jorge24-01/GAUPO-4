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
@Table(name = "mardor_personalizado")
public class MarcadorPersonalizado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marcador_personalizado")
    private Long id;
    @Column(name = "nombre", length = 50)
    private String nombre;
    @Column(name = "latitud",nullable = false)
    private Double latitud;
    @Column(name = "longitud",nullable = false)
    private Double longitud;
    @Column(name = "notas", length = 500)
    private String notas;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
