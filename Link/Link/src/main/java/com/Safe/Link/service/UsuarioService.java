package com.Safe.Link.service;

import com.Safe.Link.DTO.LoginResponseDTO;
import com.Safe.Link.DTO.SesionDTO;
import com.Safe.Link.DTO.UsuarioDTO;
import com.Safe.Link.entities.Usuario;
import com.Safe.Link.repositories.UsuarioRepository;
import com.Safe.Link.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO create(UsuarioDTO dto){
        if (dto.getNombre()==null || dto.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (dto.getCorreo()== null || dto.getCorreo().isBlank())
            throw new IllegalArgumentException("El correo es obligatorio");
        if (dto.getContrasena() == null || dto.getContrasena().isBlank())
            throw new IllegalArgumentException("La contrasena es obligatoria");
        if (repository.existsByCorreo(dto.getCorreo()))
            throw new IllegalArgumentException("El correo ya esta registrado");
        return toDTO(repository.save(toEntity(dto)));
    }

    public LoginResponseDTO login(SesionDTO dto){
        if (dto.getCorreo() == null || dto.getContrasena() == null)
            throw new IllegalArgumentException("Correo y contrasena son obligatorios");

        Usuario u = repository.findByCorreo(dto.getCorreo())
                .orElseThrow(()->new IllegalArgumentException("Correo o contraseña incorrectos"));

        String contrasenaGuardada = u.getContrasena();
        boolean passwordMatches = contrasenaGuardada != null
                && passwordEncoder.matches(dto.getContrasena(), contrasenaGuardada);

        if (!passwordMatches)
            throw new IllegalArgumentException("Correo o contrasena incorrectos");

        String token = jwtUtil.generateToken(u.getCorreo());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(u.getId());
        response.setNombre(u.getNombre());
        response.setCorreo(u.getCorreo());
        response.setTipoUsuario(u.getTipo_usuario());
        response.setToken(token);
        return response;
    }

    public UsuarioDTO getById(Long id){
        return toDTO(repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Usuario no encontrado" + id)));
    }

    public List<UsuarioDTO> getByTipo(String tipo){
        List<String>validos = List.of("residente", "autoridad", "voluntario", "coordinador");
        if (!validos.contains(tipo))
            throw new IllegalArgumentException("tipo invalido: " + tipo);
        return repository.findByTipoUsuario(tipo).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<UsuarioDTO>getAll(){
        return repository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public UsuarioDTO update(Long id, UsuarioDTO dto){
        Usuario u= repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Usuario no encontrado: " + id));
        u.setNombre(dto.getNombre()); u.setApellido(dto.getApellido());
        u.setEdad(dto.getEdad()); u.setTelefono(dto.getTelefono());
        u.setTipo_usuario(dto.getTipoUsuario()); u.setDistrito(dto.getDistrito());
        return toDTO(repository.save(u));
    }

    public void delete(Long id){
        if (!repository.existsById(id))
            throw new IllegalArgumentException("Usuario no encontrado: " + id);
        repository.deleteById(id);
    }

    private UsuarioDTO toDTO(Usuario e){
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(e.getId()); dto.setNombre(e.getNombre());
        dto.setApellido(e.getApellido()); dto.setEdad(e.getEdad());
        dto.setCorreo(e.getCorreo()); dto.setTelefono(e.getTelefono());
        dto.setTipoUsuario(e.getTipo_usuario()); dto.setDistrito(e.getDistrito());
        dto.setFechaRegistro(e.getFecha_registro());
        return dto;
    }

    private Usuario toEntity(UsuarioDTO dto){
        Usuario e = new Usuario();
        e.setNombre(dto.getNombre()); e.setApellido(dto.getApellido());
        e.setEdad(dto.getEdad()); e.setCorreo(dto.getCorreo());
        e.setTelefono(dto.getTelefono()); e.setTipo_usuario(dto.getTipoUsuario());
        e.setDistrito(dto.getDistrito());
        e.setFecha_registro(dto.getFechaRegistro() != null ? dto.getFechaRegistro() : LocalDate.now());
        e.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        return e;
    }
}
