package com.Safe.Link.repositories;

import com.Safe.Link.entities.Miembros_de_familia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Miembros_de_familia_repository extends JpaRepository<Miembros_de_familia, Long> {
    @Query("SELECT m FROM Miembros_de_familia m WHERE m.id_familia = :idFamilia")
    List<Miembros_de_familia>findByIdFamilia(@Param("idFamilia") Long idFamilia);
}
