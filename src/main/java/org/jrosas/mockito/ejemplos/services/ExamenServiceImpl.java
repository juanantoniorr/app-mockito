package org.jrosas.mockito.ejemplos.services;

import org.jrosas.mockito.ejemplos.models.Examen;
import org.jrosas.mockito.ejemplos.repositories.IExamenRepository;
import org.jrosas.mockito.ejemplos.repositories.IPreguntasRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements IExamenService  {

    private IExamenRepository examenRepository;
    private IPreguntasRepository preguntasRepository;

    public ExamenServiceImpl(IExamenRepository examenRepository, IPreguntasRepository preguntasRepository) {
        this.examenRepository = examenRepository;
        this.preguntasRepository = preguntasRepository;
    }

    public Optional <Examen> findByName(String nombre) {
        //Los Opcional hay que agarrarlos con un metodo get
       return examenRepository.findAll().stream()
               .filter(n-> n.getNombre().equals(nombre))
               .findFirst(); 
    }

	@Override
	public Examen findExamenByNombreForPreguntas(String nombre) {
		Optional <Examen> examenOptional = findByName(nombre);
		Examen examen = null;
		if (examenOptional.isPresent()) {
			examen = examenOptional.get();
			System.out.println("Examen Id " + examen.getId());
			List <String> preguntas = preguntasRepository.findByIdExam(examen.getId());
			System.out.println("Lista preguntas " + preguntas.size());
			examen.setPreguntas(preguntas);	
		}
	
		return examen;
	}
}
