package com.examen.partelorenzo.DTOS;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AlertaDTO {

    private String id_alerta;

    private String id_usuario;

    private String tipo_alerta;

    private String mensaje;

}
