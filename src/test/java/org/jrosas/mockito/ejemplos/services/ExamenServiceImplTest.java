package org.jrosas.mockito.ejemplos.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jrosas.mockito.ejemplos.models.Examen;
import org.jrosas.mockito.ejemplos.repositories.IExamenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*; //De esta forma no necesito poner la clase antes del metodo estatico

class ExamenServiceImplTest {
	
	@Test
	void findExamenByNombre() {
		//Mockito provides a mock implementation of IExamenRepository
		//It will never invoke the real implementation
		IExamenRepository repository = Mockito.mock(IExamenRepository.class);
		IExamenService service = new ExamenServiceImpl(repository);
		List <Examen> datos  = Arrays.asList(new Examen(5L,"Matematicas"), new Examen(6L, "Lenguajes"), new Examen(7L,"Ciencias") );
		Mockito.when (repository.findAll()).thenReturn(datos);
		Optional <Examen> examen = service.findByName("Ciencias");
		assertTrue(examen.isPresent());
		assertEquals(7, examen.get().getId());
		assertEquals("Ciencias", examen.get().getNombre());
	}
	
	@Test
	void findExamenByNombreListaVacia() {
		//Mockito provides a mock implementation of IExamenRepository
		//It will never invoke the real implementation
		IExamenRepository repository = Mockito.mock(IExamenRepository.class);
		IExamenService service = new ExamenServiceImpl(repository);
		List <Examen> datos  = Collections.emptyList();
		Mockito.when (repository.findAll()).thenReturn(datos);
		Optional <Examen> examen = service.findByName("Ciencias");
		assertFalse(examen.isPresent());
	}

}