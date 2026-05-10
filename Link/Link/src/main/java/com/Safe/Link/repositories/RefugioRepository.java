package com.Safe.Link.repositories;

import com.Safe.Link.entities.Refugio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefugioRepository extends JpaRepository<Refugio, Long> {
    @Query("SELECT r FROM Refugio r WHERE r.disponible = true ORDER BY r.capacidadMaxima DESC ")
    List<Refugio>findDisponible();

    @Query("SELECT r FROM Refugio r WHERE r.id = :id AND r.disponible = true")
    Optional<Refugio>findDisponibleById(@Param("id")Long id);
}
