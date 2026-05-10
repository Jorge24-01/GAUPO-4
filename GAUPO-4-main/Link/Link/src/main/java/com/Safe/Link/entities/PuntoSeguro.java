package com.Safe.Link.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "punto_seguro")
public class PuntoSeguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPunto;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private String nombre;

    private String tipo;

    private Double latitud;

    private Double longitud;

    private String direccion;

    private Double capacidad;

    private Boolean esOficial;

    public Long getIdPunto() {
        return idPunto;
    }

    public void setIdPunto(Long idPunto) {
        this.idPunto = idPunto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Double capacidad) {
        this.capacidad = capacidad;
    }

    public Boolean getEsOficial() {
        return esOficial;
    }

    public void setEsOficial(Boolean esOficial) {
        this.esOficial = esOficial;
    }
}
