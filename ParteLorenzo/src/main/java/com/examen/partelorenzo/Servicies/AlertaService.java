package com.examen.partelorenzo.Servicies;

import com.examen.partelorenzo.entities.Alerta;
import com.examen.partelorenzo.repositories.Alerta_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertaService {
    @Autowired
    private Alerta_repository listarTodas(){
        return listarTodas();
    }

    public Alerta guardar(Alerta alerta){
        return listarTodas().save(alerta);
    }
}
