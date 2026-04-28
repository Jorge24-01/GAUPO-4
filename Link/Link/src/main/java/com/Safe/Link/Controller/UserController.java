package com.Safe.Link.Controller;

import com.Safe.Link.DTO.UserDTO;
import com.Safe.Link.Services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping
    public List<UserDTO> listar() { return userService.listar(); }

    @GetMapping("/{id}")
    public UserDTO obtenerPorId(@PathVariable Long id) { return userService.obtenerPorId(id); }

    @PostMapping
    public UserDTO crear(@RequestBody UserDTO dto) { return userService.crear(dto); }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        userService.eliminar(id);
    }
}