package org.jrosas.mockito.ejemplos.repositories;

import org.jrosas.mockito.ejemplos.models.Examen;

import java.util.List;

public interface IExamenRepository {
    List<Examen> findAll();
    Examen save (Examen examen);
}
