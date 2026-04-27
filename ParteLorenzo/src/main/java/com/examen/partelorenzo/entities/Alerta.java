package com.examen.partelorenzo.entities;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Alerta")
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private String id_alerta;
    @Column(nullable = false)
    private String id_usuario;
    @Column(nullable = false)
    private String tipo_alerta;
    @Column(nullable = false)
    private String mensaje;

}
