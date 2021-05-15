package org.jrosas.mockito.ejemplos.services;

import java.util.Optional;

import org.jrosas.mockito.ejemplos.models.Examen;

public interface IExamenService {
    Optional <Examen> findByName (String nombre);
    Examen findExamenByNombreForPreguntas(String nombre);
}
