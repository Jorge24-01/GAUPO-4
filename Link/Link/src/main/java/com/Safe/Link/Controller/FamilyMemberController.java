package com.Safe.Link.Controller;

import com.Safe.Link.DTO.FamilyMemberDTO;
import com.Safe.Link.Services.FamilyMemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/familymembers")
public class FamilyMemberController {

    private final FamilyMemberService familyMemberService;

    public FamilyMemberController(FamilyMemberService familyMemberService) { this.familyMemberService = familyMemberService; }

    @GetMapping
    public List<FamilyMemberDTO> listar() { return familyMemberService.listar(); }

    @GetMapping("/{id}")
    public FamilyMemberDTO obtenerPorId(@PathVariable Long id) { return familyMemberService.obtenerPorId(id); }

    @GetMapping("/por-user/{userId}")
    public List<FamilyMemberDTO> porUser(@PathVariable Long userId) { return familyMemberService.porUser(userId); }

    @PostMapping("/user/{userId}")
    public FamilyMemberDTO crearParaUser(
            @PathVariable Long userId,
            @RequestBody FamilyMemberDTO dto) {
        return familyMemberService.crearParaUser(userId, dto);
    }
}