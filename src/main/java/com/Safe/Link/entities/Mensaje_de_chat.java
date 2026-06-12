package com.Safe.Link.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Mensaje_de_chat")
public class Mensaje_de_chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje_de_chat")
    private Long id_mensaje;
    @Column(nullable = false)
    private Long id_Usuario;
    @Column(nullable = false)
    private String Contenido;
    @Column(nullable = false)
    private LocalDateTime fecha_hora;
    @Column(nullable = false)
    private String enviado_offline;
    @Column(nullable = false)
    private String tipo_canal;
}
