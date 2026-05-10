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
@Table(name = "notificaciones")
public class Notificaciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificaciones")
    private Long id;
    @Column(name = "tipo",nullable = false,length = 20)
    private String tipo;
    @Column(name = "leido", nullable = false)
    private Boolean leido;
    @Column(name = "fecha_hora", nullable = false)
    private LocalDate fecha_hora;

    @ManyToOne
    @JoinColumn(name = "id_usuario_destino",nullable = false)
    private Usuario usuarioDestino;

    @ManyToOne
    @JoinColumn(name = "id_alerta")
    private Alerta alerta;

}
