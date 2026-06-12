package com.Safe.Link.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alerta")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Long id;
    @Column(name = "tipo_alerta", nullable = false, length = 60)
    private String tipoAlerta;
    @Column(name = "mensaje", length = 500)
    private String mensaje;
    @Column(name = "latitud", nullable = false)
    private Double latitud;
    @Column(name = "longitud", nullable = false)
    private Double longitud;
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fecha_hora = LocalDateTime.now();
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

}
