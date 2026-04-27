package com.examen.partelorenzo.Servicies;

import com.examen.partelorenzo.entities.Miembros_de_Familia;
import com.examen.partelorenzo.repositories.Miembros_de_Familia_repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class Miembros_de_Familia_service {
    @Autowired
    private Miembros_de_Familia_repository repository;
    private Miembros_de_Familia_repository Miembros_de_Familia;

    public List<Miembros_de_Familia> listarPorFamilia(String idFamilia) {
        return repository.findByIdFamilia(idFamilia);
    }
    public Miembros_de_Familia registrarMiembro(Miembros_de_Familia miembro) {
        return null;
    }

    public Miembros_de_Familia_repository eliminarMiembro(Miembros_de_Familia miembro) {
        return repository;
    }

}
