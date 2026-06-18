package com.examen.partelorenzo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Mensaje_de_chat")
public class Mensaje_de_chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id_mensaje;
    @Column(nullable = false)
    private String id_Usuario;
    @Column(nullable = false)
    private String Contenido;
    @Column(nullable = false)
    private LocalDateTime fecha_hora;
    @Column(nullable = false)
    private String enviado_offline;
    @Column(nullable = false)
    private String tipo_canal;

}
