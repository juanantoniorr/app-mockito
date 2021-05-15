package org.jrosas.mockito.ejemplos.models;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


public class Examen {
   @NonNull private @Getter @Setter Long id;
    private @Getter @Setter String nombre;
    private @Getter @Setter List <String> preguntas;


    public Examen(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        preguntas = new ArrayList<String>();
    }
}
