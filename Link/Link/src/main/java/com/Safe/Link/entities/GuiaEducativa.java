package com.Safe.Link.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "guias_educativas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuiaEducativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    @Column(length = 100)
    private String categoria; // Primeros Auxilios, Seguridad en el Hogar, Kit de Emergencia

    @Column(name = "tipo_desastre", length = 50)
    private String tipoDesastre; // Sismo, Inundacion, Incendio, Huaico, Tsunami

    @Column(name = "fase_temporal", length = 20)
    private String faseTemporal; // Antes, Durante, Despues

    @Column(name = "url_imagen", length = 500)
    private String urlImagen;

    @Column(name = "disponible_offline")
    private Boolean disponibleOffline = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }
}
