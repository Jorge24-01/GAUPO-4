package com.Safe.Link.repositories;

import com.Safe.Link.entities.ReporteProblema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReporteProblemaRepository extends JpaRepository<ReporteProblema, Long> {
    List<ReporteProblema> findByUsuarioId(Long usuarioId);
    List<ReporteProblema> findByEstado(String estado);
}
