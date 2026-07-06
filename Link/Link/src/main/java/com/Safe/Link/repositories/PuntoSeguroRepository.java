package com.Safe.Link.repositories;

import com.Safe.Link.entities.PuntoSeguro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuntoSeguroRepository extends JpaRepository<PuntoSeguro, Long> {
    @Query("SELECT p FROM PuntoSeguro p ORDER BY p.nombre ASC")
    List<PuntoSeguro>findAllOrdenados();

    List<PuntoSeguro> findByUsuarioIsNullOrderByNombreAsc();

    @Query("SELECT p FROM PuntoSeguro p WHERE p.usuario IS NULL OR p.usuario.id = :idUsuario ORDER BY p.nombre ASC")
    List<PuntoSeguro> findMapaByUsuario(@Param("idUsuario") Long idUsuario);

    @Query("SELECT p FROM PuntoSeguro p WHERE p.usuario.id = :idUsuario ORDER BY p.nombre ASC")
    List<PuntoSeguro> findPropiosByUsuario(@Param("idUsuario") Long idUsuario);

    @Query("SELECT p FROM PuntoSeguro p WHERE p.tipo = :tipo")
    List<PuntoSeguro>findByTipo(@Param("tipo") String tipo);

    @Query("SELECT p FROM PuntoSeguro p WHERE p.tipo = :tipo AND p.usuario IS NULL")
    List<PuntoSeguro> findSugeridosByTipo(@Param("tipo") String tipo);

    @Query("SELECT p FROM PuntoSeguro p WHERE p.tipo IN ('zona_segura', 'parque', 'area_abierta')")
    List<PuntoSeguro>findZonasSeguras();
}
