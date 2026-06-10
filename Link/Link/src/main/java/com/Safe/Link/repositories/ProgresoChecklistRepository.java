package com.Safe.Link.repositories;

import com.Safe.Link.entities.ProgresoChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProgresoChecklistRepository extends JpaRepository<ProgresoChecklist, Long> {
    List<ProgresoChecklist> findByUsuarioId(Long usuarioId);
    List<ProgresoChecklist> findByUsuarioIdAndCompletado(Long usuarioId, Boolean completado);
    void deleteByUsuarioIdAndNombreItem(Long usuarioId, String nombreItem);
}
