package com.examen.partelorenzo.repositories;

import com.examen.partelorenzo.entities.Miembros_de_Familia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Miembros_de_Familia_repository extends JpaRepository<Miembros_de_Familia_repository, String > {
    List<Miembros_de_Familia> findByIdFamilia(String id_familia);
}
