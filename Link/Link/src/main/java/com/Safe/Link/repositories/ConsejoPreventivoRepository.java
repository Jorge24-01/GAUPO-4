package com.Safe.Link.repositories;

import com.Safe.Link.entities.ConsejoPreventivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConsejoPreventivoRepository extends JpaRepository<ConsejoPreventivo, Long> {
    List<ConsejoPreventivo> findByTipoDesastre(String tipoDesastre);
    List<ConsejoPreventivo> findByCategoria(String categoria);
    List<ConsejoPreventivo> findAllByOrderByOrdenVisualizacionAsc();
}
