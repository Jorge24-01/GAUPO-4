package com.Safe.Link.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre",nullable = false,length = 100)
    private String nombre;
    @Column(name = "apellido",nullable = false, length = 100)
    private String apellido;
    @Column(name = "edad", nullable = false)
    private int edad;
    @Column(name = "correo", nullable = false, length = 150)
    private String correo;
    @Column(name = "telefono", nullable = false,length = 20)
    private String telefono;
    @Column(name = "tipo_usuario",nullable = false,length = 30)
    private String tipo_usuario;
    @Column(name = "distrito", length = 150)
    private String distrito;
    @Column(name = "fecha_registro",nullable = false)
    private LocalDate fecha_registro;
}
