package org.jrosas.mockito.ejemplos.services;

import org.jrosas.mockito.ejemplos.models.Examen;
import org.jrosas.mockito.ejemplos.repositories.IExamenRepository;

import java.util.Optional;

public class ExamenServiceImpl implements IExamenService  {

    private IExamenRepository examenRepository;

    public ExamenServiceImpl(IExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    public Examen findByName(String nombre) {
        //Los Opcional hay que agarrarlos con un metodo get
       Optional<Examen> examenOptional = examenRepository.findAll().stream()
               .filter(n-> n.getNombre().equals(nombre))
               .findFirst();
       Examen examen = null;
       if (examenOptional.isPresent()){
           examen = examenOptional.get();
           return examen;
       }

       return examen;


    }
}
