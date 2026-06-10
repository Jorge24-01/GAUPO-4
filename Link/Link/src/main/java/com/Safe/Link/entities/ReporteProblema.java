package com.Safe.Link.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes_problema")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteProblema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(length = 50)
    private String estado; // PENDIENTE, REVISADO, RESUELTO

    @Column(name = "fecha_reporte")
    private LocalDateTime fechaReporte;

    @Column(name = "pantalla_afectada", length = 100)
    private String pantallaAfectada;

    @PrePersist
    public void prePersist() {
        this.fechaReporte = LocalDateTime.now();
        if (this.estado == null) this.estado = "PENDIENTE";
    }
}
