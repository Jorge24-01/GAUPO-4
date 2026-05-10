package com.Safe.Link.DTO;

import com.Safe.Link.entities.KitEmergencias;

import java.time.LocalDate;

public class KitEmergenciasDTO {

    private Long idKit;
    private Long idUsuario;
    private LocalDate fechaUltimaRevision;
    private Boolean completado;

    public static KitEmergenciasDTO fromEntity(KitEmergencias kit) {
        KitEmergenciasDTO dto = new KitEmergenciasDTO();
        dto.setIdKit(kit.getIdKit());

        if (kit.getUsuario() != null) {
            dto.setIdUsuario(kit.getUsuario().getId());
        }

        dto.setFechaUltimaRevision(kit.getFechaUltimaRevision());
        dto.setCompletado(kit.getCompletado());
        return dto;
    }

    public Long getIdKit() {
        return idKit;
    }

    public void setIdKit(Long idKit) {
        this.idKit = idKit;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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
}
