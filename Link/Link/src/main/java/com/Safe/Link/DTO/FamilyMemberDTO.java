package com.Safe.Link.DTO;

import com.Safe.Link.Entities.FamilyMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor

public class FamilyMemberDTO {

    private Long id;
    private String nombre;
    private String phone;
    private String relacion;
    private Long userId;

    // Entity -> DTO
    public static FamilyMemberDTO fromEntity(FamilyMember f) {
        Long uid = f.getUser() == null ? null : f.getUser().getId();

        return new FamilyMemberDTO(
                f.getId(),
                f.getNombre(),
                f.getPhone(),
                f.getRelacion(),
                uid
        );
    }

    // DTO -> Entity
    public FamilyMember toEntity() {
        FamilyMember f = new FamilyMember();
        f.setId(this.id);
        f.setNombre(this.nombre);
        f.setPhone(this.phone);
        f.setRelacion(this.relacion);
        return f;
    }
}