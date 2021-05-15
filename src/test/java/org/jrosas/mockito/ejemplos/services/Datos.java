package org.jrosas.mockito.ejemplos.services;

import java.util.Arrays;
import java.util.List;

import org.jrosas.mockito.ejemplos.models.Examen;

public class Datos {
public final static List <Examen> EXAMENES = Arrays.asList(new Examen(5L,"Matematicas"), new Examen(6L, "Lenguajes"), new Examen(7L,"Ciencias") );

public final static List<String> PREGUNTAS = Arrays.asList("aritmetica", "integrales", "derivadas");
}
