package com.Safe.Link.repositories;

import com.Safe.Link.entities.FaqPregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FaqPreguntaRepository extends JpaRepository<FaqPregunta, Long> {
    List<FaqPregunta> findByCategoria(String categoria);
    List<FaqPregunta> findAllByOrderByOrdenRelevanciaAsc();
}
