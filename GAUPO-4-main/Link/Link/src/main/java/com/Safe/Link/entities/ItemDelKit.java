package com.Safe.Link.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "item_del_kit")
public class ItemDelKit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItem;

    @ManyToOne
    @JoinColumn(name = "id_kit")
    private KitEmergencias kitEmergencias;

    private String nombreItem;

    private String categoria;

    private Boolean tieneItem;

    private LocalDate fechaVencimiento;

    public Long getIdItem() {
        return idItem;
    }

    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }

    public KitEmergencias getKitEmergencias() {
        return kitEmergencias;
    }

    public void setKitEmergencias(KitEmergencias kitEmergencias) {
        this.kitEmergencias = kitEmergencias;
    }

    public String getNombreItem() {
        return nombreItem;
    }

    public void setNombreItem(String nombreItem) {
        this.nombreItem = nombreItem;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getTieneItem() {
        return tieneItem;
    }

    public void setTieneItem(Boolean tieneItem) {
        this.tieneItem = tieneItem;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}
