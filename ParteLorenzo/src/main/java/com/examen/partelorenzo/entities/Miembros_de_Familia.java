package com.examen.partelorenzo.entities;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Miembros_de_Familia")
public class Miembros_de_Familia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id_miembro;
    @Column(nullable = false)
    private String id_familia;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private int edad;
    @Column(nullable = false)
    private String parentezco;
    @Column(nullable = false)
    private int contacto;
}
