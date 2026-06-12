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
@Table(name = "Miembros_de_Familia")
public class Miembros_de_familia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_miembro")
    private Long id_miembro;
    @Column(name = "id_Familia",nullable = false)
    private Long id_familia;
    @Column(name = "nombre",nullable = false)
    private String nombre;
    @Column(name = "edad",nullable = false)
    private int edad;
    @Column(name = "parentezco",nullable = false)
    private String parentezco;
    @Column(name = "contacto",nullable = false)
    private int contacto;
}
