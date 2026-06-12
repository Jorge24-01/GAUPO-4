package com.Safe.Link.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progreso_checklist")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgresoChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "nombre_item", nullable = false, length = 200)
    private String nombreItem;

    @Column(name = "es_personalizado")
    private Boolean esPersonalizado = false;

    @Column(name = "completado")
    private Boolean completado = false;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
