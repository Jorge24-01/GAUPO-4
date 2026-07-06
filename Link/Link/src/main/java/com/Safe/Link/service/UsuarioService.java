package com.Safe.Link.service;

import com.Safe.Link.DTO.LoginResponseDTO;
import com.Safe.Link.DTO.SesionDTO;
import com.Safe.Link.DTO.UsuarioDTO;
import com.Safe.Link.entities.Familia;
import com.Safe.Link.entities.KitEmergencia;
import com.Safe.Link.entities.Usuario;
import com.Safe.Link.repositories.FamiliaRepository;
import com.Safe.Link.repositories.ItemKitRepository;
import com.Safe.Link.repositories.KitEmergenciaRepository;
import com.Safe.Link.repositories.Miembros_de_familia_repository;
import com.Safe.Link.repositories.UsuarioRepository;
import com.Safe.Link.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private static final String DEMO_EMAIL = "demo@safelink.com";
    private static final List<String> TIPOS_REGISTRO = List.of("RESIDENTE", "AUTORIDAD", "VOLUNTARIO", "COORDINADOR");
    private static final List<String> TIPOS_CONSULTA = List.of("ADMIN", "RESIDENTE", "AUTORIDAD", "VOLUNTARIO", "COORDINADOR");

    private final UsuarioRepository repository;
    private final FamiliaRepository familiaRepository;
    private final Miembros_de_familia_repository miembroRepository;
    private final KitEmergenciaRepository kitRepository;
    private final ItemKitRepository itemKitRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO create(UsuarioDTO dto){
        if (dto.getNombre()==null || dto.getNombre().isBlank())
            throw new IllegalArgumentException("nombre.obligatorio");
        if (dto.getCorreo()== null || dto.getCorreo().isBlank())
            throw new IllegalArgumentException("usuario.correo.obligatorio");
        if (dto.getContrasena() == null || dto.getContrasena().isBlank())
            throw new IllegalArgumentException("usuario.contrasena.obligatoria");
        if (repository.existsByCorreo(dto.getCorreo()))
            throw new IllegalArgumentException("usuario.correo.duplicado");
        dto.setTipoUsuario(normalizarTipoRegistro(dto.getTipoUsuario()));
        return toDTO(repository.save(toEntity(dto)));
    }

    public LoginResponseDTO login(SesionDTO dto){
        if (dto.getCorreo() == null || dto.getContrasena() == null)
            throw new IllegalArgumentException("usuario.credenciales.obligatorias");

        Usuario u = repository.findByCorreo(dto.getCorreo())
                .orElseThrow(()->new IllegalArgumentException("usuario.credenciales.invalidas"));

        String contrasenaGuardada = u.getContrasena();
        boolean passwordMatches = contrasenaGuardada != null
                && passwordEncoder.matches(dto.getContrasena(), contrasenaGuardada);

        if (!passwordMatches)
            throw new IllegalArgumentException("usuario.credenciales.invalidas");

        String token = jwtUtil.generateToken(u.getCorreo());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(u.getId());
        response.setNombre(u.getNombre());
        response.setApellido(u.getApellido());
        response.setCorreo(u.getCorreo());
        response.setTipoUsuario(u.getTipo_usuario());
        response.setToken(token);
        return response;
    }

    public UsuarioDTO getById(Long id){
        return toDTO(repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("usuario.no.encontrado")));
    }

    public List<UsuarioDTO> getByTipo(String tipo){
        String tipoNormalizado = normalizarTipoConsulta(tipo);
        if (!TIPOS_CONSULTA.contains(tipoNormalizado))
            throw new IllegalArgumentException("usuario.tipo.invalido");
        return repository.findByTipoUsuario(tipoNormalizado).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<UsuarioDTO>getAll(){
        return repository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public UsuarioDTO update(Long id, UsuarioDTO dto){
        Usuario u= repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("usuario.no.encontrado"));
        u.setNombre(dto.getNombre()); u.setApellido(dto.getApellido());
        u.setEdad(dto.getEdad()); u.setTelefono(dto.getTelefono());
        if (dto.getTipoUsuario() != null && !dto.getTipoUsuario().isBlank()) {
            u.setTipo_usuario(normalizarTipoRegistro(dto.getTipoUsuario()));
        }
        u.setDistrito(dto.getDistrito());
        return toDTO(repository.save(u));
    }

    @Transactional
    public void delete(Long id){
        Usuario usuario = repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("usuario.no.encontrado"));

        if (DEMO_EMAIL.equalsIgnoreCase(usuario.getCorreo())) {
            throw new IllegalArgumentException("usuario.admin.no.eliminable");
        }

        familiaRepository.findByUsuarioId(id).ifPresent(familia -> {
            miembroRepository.deleteAll(miembroRepository.findByIdFamilia(familia.getId()));
            familiaRepository.delete(familia);
        });

        kitRepository.findByUsuarioId(id).ifPresent(kit -> {
            itemKitRepository.deleteAll(itemKitRepository.findByKitId(kit.getId()));
            kitRepository.delete(kit);
        });

        repository.delete(usuario);
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

    private String normalizarTipoRegistro(String tipoUsuario) {
        if (tipoUsuario == null || tipoUsuario.isBlank() || "USUARIO".equalsIgnoreCase(tipoUsuario)) {
            return "RESIDENTE";
        }

        String tipoNormalizado = tipoUsuario.trim().toUpperCase(Locale.ROOT);
        if ("ADMIN".equals(tipoNormalizado)) {
            throw new IllegalArgumentException("usuario.registro.admin.no.permitido");
        }
        if (!TIPOS_REGISTRO.contains(tipoNormalizado)) {
            throw new IllegalArgumentException("usuario.tipo.invalido.valores");
        }
        return tipoNormalizado;
    }

    private String normalizarTipoConsulta(String tipoUsuario) {
        if (tipoUsuario == null) {
            return "";
        }
        return tipoUsuario.trim().toUpperCase(Locale.ROOT);
    }
}
