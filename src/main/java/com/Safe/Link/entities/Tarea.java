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
@Table(name = "tarea")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarea")
    private Long id;
    @Column(name = "descripcion", nullable = false,length = 500)
    private String descripcion;
    @Column(name = "estado", nullable = false, length = 30)
    private String estado;
    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate fecha_asignacion;

    @ManyToOne
    @JoinColumn(name = "id_voluntario", nullable = false)
    private Voluntario voluntario;

    @ManyToOne
    @JoinColumn(name = "id_coordinador", nullable = false)
    private Usuario id_coordinador;
}
