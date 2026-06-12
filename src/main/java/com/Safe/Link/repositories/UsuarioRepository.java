package com.Safe.Link.repositories;

import com.Safe.Link.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByCorreo(String correo);

    @Query("SELECT u FROM Usuario u WHERE u.correo = :correo")
    Optional<Usuario> findByCorreo(@Param("correo")String correo);

    @Query("SELECT u FROM Usuario u WHERE u.tipo_usuario = :tipo")
    List<Usuario>findByTipoUsuario(@Param("tipo") String tipo);
}
