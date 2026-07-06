package com.Safe.Link.service;

import com.Safe.Link.DTO.PuntoSeguroDTO;
import com.Safe.Link.entities.PuntoSeguro;
import com.Safe.Link.entities.Usuario;
import com.Safe.Link.repositories.PuntoSeguroRepository;
import com.Safe.Link.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PuntoSeguroService {
    private final PuntoSeguroRepository repository;
    private final UsuarioRepository usuarioRepository;

    public List<PuntoSeguroDTO>getAll(){
        return repository.findByUsuarioIsNullOrderByNombreAsc().stream()
                .map(punto -> toDTO(punto, null)).collect(Collectors.toList());
    }

    public PuntoSeguroDTO getById(Long id){
        PuntoSeguro punto = repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("punto.seguro.no.encontrado"));
        if (punto.getUsuario() != null) {
            throw new IllegalArgumentException("punto.seguro.no.encontrado");
        }
        return toDTO(punto, null);
    }

    @Transactional
    public PuntoSeguroDTO create(PuntoSeguroDTO dto){
        validarBase(dto);
        return toDTO(repository.save(toEntity(dto)), null);
    }

    public List<PuntoSeguroDTO> getMapaUsuario(Long idUsuario, String correoAutenticado) {
        Usuario usuario = validarAccesoUsuario(idUsuario, correoAutenticado);
        return repository.findMapaByUsuario(idUsuario).stream()
                .map(punto -> toDTO(punto, usuario.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public PuntoSeguroDTO createUsuario(Long idUsuario, PuntoSeguroDTO dto, String correoAutenticado) {
        Usuario usuario = validarAccesoUsuario(idUsuario, correoAutenticado);
        validarBase(dto);
        if (dto.getDireccion() == null || dto.getDireccion().isBlank()) {
            throw new IllegalArgumentException("direccion.obligatoria");
        }

        PuntoSeguro punto = new PuntoSeguro();
        punto.setNombre(dto.getNombre().trim());
        punto.setTipo(normalizarTipo(dto.getTipo()));
        punto.setLatitud(dto.getLatitud());
        punto.setLongitud(dto.getLongitud());
        punto.setDireccion(dto.getDireccion().trim());
        punto.setCapacidad(dto.getCapacidad());
        punto.setEsOficial(false);
        punto.setUsuario(usuario);

        return toDTO(repository.save(punto), usuario.getId());
    }

    @Transactional
    public void deleteUsuario(Long idUsuario, Long idPunto, String correoAutenticado) {
        validarAccesoUsuario(idUsuario, correoAutenticado);

        PuntoSeguro punto = repository.findById(idPunto)
                .orElseThrow(() -> new IllegalArgumentException("punto.seguro.no.encontrado"));
        Usuario usuarioPunto = punto.getUsuario();
        if (usuarioPunto == null) {
            throw new AccessDeniedException("punto.seguro.no.eliminable.sugerido");
        }
        if (!usuarioPunto.getId().equals(idUsuario)) {
            throw new AccessDeniedException("punto.seguro.no.eliminable.ajeno");
        }

        repository.delete(punto);
    }

    public List<PuntoSeguroDTO>getBytipo(String tipo){
        List<String>valido=List.of("refugio", "punto de encuentro","zona segura");
        if (!valido.contains(tipo))
            throw new IllegalArgumentException("tipo.invalido");
        return repository.findSugeridosByTipo(tipo).stream()
                .map(punto -> toDTO(punto, null)).collect(Collectors.toList());

    }


    @Transactional
    public PuntoSeguroDTO update(Long id, PuntoSeguroDTO dto){
        PuntoSeguro u = repository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("punto.seguro.no.encontrado"));
        u.setNombre(dto.getNombre());
        u.setTipo(dto.getTipo()); u.setLatitud(dto.getLatitud());
        u.setLongitud(dto.getLongitud()); u.setDireccion(dto.getDireccion());
        return toDTO(repository.save(u), null);
    }

    @Transactional
    public void delete(Long id){
        if (!repository.existsById(id))
            throw new IllegalArgumentException("punto.seguro.no.encontrado");
        repository.deleteById(id);
    }


    private PuntoSeguroDTO toDTO(PuntoSeguro e, Long idUsuarioConsultado){
        PuntoSeguroDTO dto = new PuntoSeguroDTO();
        Usuario usuario = e.getUsuario();
        Long idUsuario = usuario != null ? usuario.getId() : null;
        dto.setId(e.getId()); dto.setNombre(e.getNombre());
        dto.setTipo(e.getTipo()); dto.setLatitud(e.getLatitud());
        dto.setLongitud(e.getLongitud()); dto.setDireccion(e.getDireccion());
        dto.setCapacidad(e.getCapacidad()); dto.setEsOficial(e.getEsOficial());
        dto.setIdUsuario(idUsuario);
        dto.setSugerido(usuario == null);
        dto.setPropio(idUsuario != null && idUsuario.equals(idUsuarioConsultado));
        return dto;
    }

    private PuntoSeguro toEntity(PuntoSeguroDTO dto){
        PuntoSeguro e = new PuntoSeguro();
        e.setNombre(dto.getNombre().trim()); e.setTipo(normalizarTipo(dto.getTipo()));
        e.setLatitud(dto.getLatitud()); e.setLongitud(dto.getLongitud());
        e.setDireccion(dto.getDireccion()); e.setCapacidad(dto.getCapacidad());
        e.setEsOficial(dto.getEsOficial() != null ? dto.getEsOficial(): true);
        return e;
    }

    private void validarBase(PuntoSeguroDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank())
            throw new IllegalArgumentException("nombre.obligatorio");
        if (dto.getLatitud() == null || dto.getLongitud() == null)
            throw new IllegalArgumentException("coordenadas.obligatorias");
    }

    private String normalizarTipo(String tipo) {
        return tipo == null || tipo.isBlank() ? "Punto seguro" : tipo.trim();
    }

    private Usuario validarAccesoUsuario(Long idUsuario, String correoAutenticado) {
        Usuario autenticado = obtenerUsuarioAutenticado(correoAutenticado);
        boolean admin = "ADMIN".equalsIgnoreCase(autenticado.getTipo_usuario());
        if (!admin && !autenticado.getId().equals(idUsuario)) {
            throw new AccessDeniedException("punto.seguro.acceso.denegado");
        }
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("usuario.no.encontrado"));
    }

    private Usuario obtenerUsuarioAutenticado(String correoAutenticado) {
        if (correoAutenticado == null || correoAutenticado.isBlank()) {
            throw new AccessDeniedException("usuario.no.autenticado");
        }
        return usuarioRepository.findByCorreo(correoAutenticado)
                .orElseThrow(() -> new AccessDeniedException("usuario.autenticado.no.encontrado"));
    }

}
