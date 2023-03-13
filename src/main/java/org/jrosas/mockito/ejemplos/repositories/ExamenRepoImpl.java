package org.jrosas.mockito.ejemplos.repositories;

import org.jrosas.mockito.ejemplos.Datos;
import org.jrosas.mockito.ejemplos.models.Examen;

import java.util.List;

public class ExamenRepoImpl implements IExamenRepository {
    @Override
    public List<Examen> findAll() {
        return Datos.EXAMENES;
    }

    @Override
    public Examen save(Examen examen) {
        return Datos.EXAMENES.get(0);
    }

    @Override
    public Examen findById(Long id) {
        //Getting lenguajes
        return Datos.EXAMENES.get(1);
    }
}
