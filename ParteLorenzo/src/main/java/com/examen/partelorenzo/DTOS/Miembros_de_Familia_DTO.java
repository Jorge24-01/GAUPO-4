package com.examen.partelorenzo.DTOS;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class Miembros_de_Familia_DTO {
    private String id_mienbro;
    private String id_familia;
    private String nombre;
    private int edad;
    private String parentezco;
    private int contacto;
}
