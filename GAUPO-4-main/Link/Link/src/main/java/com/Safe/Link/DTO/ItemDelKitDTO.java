package com.Safe.Link.DTO;

import java.time.LocalDate;

public class ItemDelKitDTO {

    private Long idItem;
    private Long idKit;
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

    public Long getIdKit() {
        return idKit;
    }

    public void setIdKit(Long idKit) {
        this.idKit = idKit;
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
