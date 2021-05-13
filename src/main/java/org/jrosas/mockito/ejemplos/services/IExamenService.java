package org.jrosas.mockito.ejemplos.services;

import org.jrosas.mockito.ejemplos.models.Examen;

public interface IExamenService {
    Examen findByName (String nombre);
}
