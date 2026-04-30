package com.Safe.Link.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "kit_emergencias")
public class KitEmergencias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idKit;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private LocalDate fechaUltimaRevision;

    private Boolean completado;

    @OneToMany(mappedBy = "kitEmergencias", cascade = CascadeType.ALL)
    private List<ItemDelKit> items;

    public Long getIdKit() {
        return idKit;
    }

    public void setIdKit(Long idKit) {
        this.idKit = idKit;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFechaUltimaRevision() {
        return fechaUltimaRevision;
    }

    public void setFechaUltimaRevision(LocalDate fechaUltimaRevision) {
        this.fechaUltimaRevision = fechaUltimaRevision;
    }

    public Boolean getCompletado() {
        return completado;
    }

    public void setCompletado(Boolean completado) {
        this.completado = completado;
    }

    public List<ItemDelKit> getItems() {
        return items;
    }

    public void setItems(List<ItemDelKit> items) {
        this.items = items;
    }
}
