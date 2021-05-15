package org.jrosas.mockito.ejemplos.services;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jrosas.mockito.ejemplos.models.Examen;
import org.jrosas.mockito.ejemplos.repositories.IExamenRepository;
import org.jrosas.mockito.ejemplos.repositories.IPreguntasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*; //De esta forma no necesito poner la clase antes del metodo estatico

class ExamenServiceImplTest {
	@Mock
	IExamenRepository repository;
	@Mock
	IPreguntasRepository preguntasRepository;
	
	@InjectMocks //Inyecta los @Mock a la clase implementadora
	ExamenServiceImpl service;
	
	
	@BeforeEach
	void setUp() {
		//Inicia los mocks antes de cada metodo
		MockitoAnnotations.openMocks(this);
		//Mockito provides a mock implementation of IExamenRepository
				//It will never invoke the real implementation
		// repository = Mockito.mock(IExamenRepository.class);
		 //preguntasRepository = Mockito.mock(IPreguntasRepository.class);
		 //Pass dependencies by constructor
		 //service = new ExamenServiceImpl(repository, preguntasRepository);
		
		 
	}
	
	
	@Test
	void findExamenByNombre() {
		Mockito.when (repository.findAll()).thenReturn(Datos.EXAMENES);
		Optional <Examen> examen = service.findByName("Ciencias");
		assertTrue(examen.isPresent());
		assertEquals(7, examen.get().getId());
		assertEquals("Ciencias", examen.get().getNombre());
	}
	
	@Test
	void findExamenByNombreListaVacia() {
		List <Examen> datos  = Collections.emptyList();
		Mockito.when (repository.findAll()).thenReturn(datos);
		Optional <Examen> examen = service.findByName("Ciencias");
		assertFalse(examen.isPresent());
	}
	
	@Test
	void findPreguntasByExam() {
		//Cuando invoques los metodos regresas esos datos de prueba
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		//El 6L se puede sustituir con anyLong
		when(preguntasRepository.findByIdExam(6L)).thenReturn(Datos.PREGUNTAS);
		//Aqui se invocan los metodos
		Examen examen = service.findExamenByNombreForPreguntas("Lenguajes");
		System.out.println("Nombre examen " + examen.getNombre());
		 assertEquals(3, examen.getPreguntas().size());
	}
	
	@Test
	void findPreguntasByExamVerify() {
		//Cuando invoques los metodos regresas esos datos de prueba
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		//El 6L se puede sustituir con anyLong
		when(preguntasRepository.findByIdExam(6L)).thenReturn(Datos.PREGUNTAS);
		//Aqui se invocan los metodos
		Examen examen = service.findExamenByNombreForPreguntas("Lenguajes");
		// assertEquals(3, examen.getPreguntas().size());
		 verify(repository).findAll();
		 //Verifica si el metodo se uso
		 verify(preguntasRepository).findByIdExam(6L);
	}
	
	@Test
	void findPreguntasByExamVerifyListaVacia() {
		//Cuando invoques los metodos regresas esos datos de prueba
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		//El 6L se puede sustituir con anyLong
		when(preguntasRepository.findByIdExam(6L)).thenReturn(Datos.PREGUNTAS);
		//Aqui se invocan los metodos con un examen que no existe
		Examen examen = service.findExamenByNombreForPreguntas("Prueba");
		assertNull(examen);
		 verify(repository).findAll();
		 //Verifica si el metodo se uso
		 verify(preguntasRepository).findByIdExam(6L);
	}


}