package com.Safe.Link.Services;

import com.Safe.Link.DTO.FamilyMemberDTO;
import com.Safe.Link.Entities.FamilyMember;
import com.Safe.Link.Entities.User;
import com.Safe.Link.Repositories.FamilyMemberRepositories;
import com.Safe.Link.Repositories.UserRepositories;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter @Setter
public class FamilyMemberService {

    private final FamilyMemberRepositories familyMemberRepo;
    private final UserRepositories userRepo;

    public FamilyMemberService(FamilyMemberRepositories familyMemberRepo, UserRepositories userRepo) {
        this.familyMemberRepo = familyMemberRepo;
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public List<FamilyMemberDTO> listar() {
        return familyMemberRepo.findAll().stream()
                .map(FamilyMemberDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FamilyMemberDTO obtenerPorId(Long id) {
        FamilyMember f = familyMemberRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyMember con id " + id + " no encontrado"));
        return FamilyMemberDTO.fromEntity(f);
    }

    @Transactional(readOnly = true)
    public List<FamilyMemberDTO> porUser(Long userId) {
        return familyMemberRepo.findByUserId(userId).stream()
                .map(FamilyMemberDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public FamilyMemberDTO crearParaUser(Long userId, FamilyMemberDTO dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User con id " + userId + " no encontrado"));

        FamilyMember familyMember = dto.toEntity();
        familyMember.setUser(user);

        FamilyMember guardado = familyMemberRepo.save(familyMember);
        return FamilyMemberDTO.fromEntity(guardado);
    }
}