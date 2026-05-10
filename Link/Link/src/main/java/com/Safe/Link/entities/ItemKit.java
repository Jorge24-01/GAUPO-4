package com.Safe.Link.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_kit")
public class ItemKit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Long id;
    @Column(name = "nombre_item", nullable = false, length = 150)
    private String nombreItem;
    @Column(name = "categoria", nullable = false, length = 50)
    private String categoria;
    @Column(name = "cantidad_recomendada")
    private Integer cantidadRecomendada=1;
    @Column(name = "tiene_item", nullable = false)
    private Boolean tieneItem= false;
    @ManyToOne
    @JoinColumn(name = "id_kit", nullable = false)
    private KitEmergencia kit;
}
