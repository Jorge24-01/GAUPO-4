package com.Safe.Link.DTO;

import com.Safe.Link.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {

    private Long id;
    private String nombre;
    private String email;
    private String password;
    private String role;

    // Entity -> DTO
    public static UserDTO fromEntity(User u) {
        return new UserDTO(
                u.getId(),
                u.getNombre(),
                u.getEmail(),
                u.getPassword(),
                u.getRole()
        );
    }

    // DTO -> Entity
    public User toEntity() {
        User u = new User();
        u.setId(this.id);
        u.setNombre(this.nombre);
        u.setEmail(this.email);
        u.setPassword(this.password);
        u.setRole(this.role);
        return u;
    }
}