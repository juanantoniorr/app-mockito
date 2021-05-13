package org.jrosas.mockito.ejemplos.repositories;

import org.jrosas.mockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class ExamenRepositoryImpl implements IExamenRepository {

    @Override
    public List<Examen> findAll() {
        return Arrays.asList(new Examen(5L,"Matematicas"), new Examen(6L, "Lenguajes"), new Examen(7L,"Ciencias") );
    }
}
