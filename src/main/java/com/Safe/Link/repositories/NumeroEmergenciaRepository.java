package com.Safe.Link.repositories;

import com.Safe.Link.entities.NumeroEmergencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NumeroEmergenciaRepository extends JpaRepository<NumeroEmergencia, Long> {
    List<NumeroEmergencia> findAllByOrderByOrdenVisualizacionAsc();
}
