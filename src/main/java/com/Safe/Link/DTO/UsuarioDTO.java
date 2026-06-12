package com.Safe.Link.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UsuarioDTO {

   @Schema(accessMode = Schema.AccessMode.READ_ONLY)
   @JsonProperty(access = JsonProperty.Access.READ_ONLY)
   private Long id;
   private String nombre;
   private String apellido;
   private int edad;
   private String correo;
   private String contrasena;
   private String telefono;
   private String tipoUsuario;
   private String distrito;
   private LocalDate fechaRegistro;
   // US-23
   private Boolean modoAccesibilidad;
   private Boolean esAdultoMayor;
}
