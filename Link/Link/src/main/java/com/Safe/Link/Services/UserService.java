package com.Safe.Link.Services;

import com.Safe.Link.DTO.UserDTO;
import com.Safe.Link.Entities.User;
import com.Safe.Link.Repositories.UserRepositories;
import com.Safe.Link.Repositories.UserRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepositories userRepo;

    public UserService(UserRepositories userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> listar() {
        return userRepo.findAll().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO obtenerPorId(Long id) {
        User u = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User con id " + id + " no encontrado"));
        return UserDTO.fromEntity(u);
    }

    @Transactional
    public UserDTO crear(UserDTO dto) {
        User nuevo = dto.toEntity();
        User guardado = userRepo.save(nuevo);
        return UserDTO.fromEntity(guardado);
    }

    @Transactional
    public void eliminar(Long id) {
        userRepo.deleteById(id);
    }
}