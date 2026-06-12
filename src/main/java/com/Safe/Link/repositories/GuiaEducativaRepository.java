package com.Safe.Link.repositories;

import com.Safe.Link.entities.GuiaEducativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GuiaEducativaRepository extends JpaRepository<GuiaEducativa, Long> {
    List<GuiaEducativa> findByCategoria(String categoria);
    List<GuiaEducativa> findByTipoDesastre(String tipoDesastre);
    List<GuiaEducativa> findByFaseTemporal(String faseTemporal);
    List<GuiaEducativa> findByTipoDesastreAndFaseTemporal(String tipoDesastre, String faseTemporal);
    List<GuiaEducativa> findByDisponibleOffline(Boolean disponibleOffline);
}
