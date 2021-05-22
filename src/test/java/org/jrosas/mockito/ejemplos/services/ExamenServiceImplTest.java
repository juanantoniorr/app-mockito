package org.jrosas.mockito.ejemplos.services;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jrosas.mockito.ejemplos.models.Examen;
import org.jrosas.mockito.ejemplos.repositories.IExamenRepository;
import org.jrosas.mockito.ejemplos.repositories.IPreguntasRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*; //De esta forma no necesito poner la clase antes del metodo estatico

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {
	@Mock
	IExamenRepository repository;
	@Mock
	IPreguntasRepository preguntasRepository;
	
	@InjectMocks //Inyecta los @Mock a la clase implementadora
	ExamenServiceImpl service;
	
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
	
	@Test
	void saveExamTest() {
		//Esto para que pase por el metodo saveSomeQuestions
		Examen newExamen = Datos.EXAMENES.get(0);
		newExamen.setPreguntas(Datos.PREGUNTAS);
		//Cuando pase cualquier examen retornas el primero de la lista de datos
		when (repository.save(any(Examen.class))).thenReturn(Datos.EXAMENES.get(0));
		Examen examen = service.save(newExamen);
		assertNotNull(examen);
		assertEquals(5L, examen.getId());
		verify(repository).save(examen);
		verify(preguntasRepository).saveSomeQuestions(any());
		
	}
	
	@Test
	void testManejoException() {
		//Regreso lista de examenes con id nulo
		when(repository.findAll()).thenReturn(Datos.EXAMENES_NULL_ID);
		//Para cualquier argumento que sea nulo lanza el illegal argument
		when (preguntasRepository.findByIdExam(isNull())).thenThrow(IllegalArgumentException.class);
		assertThrows(IllegalArgumentException.class, () -> {
			service.findExamenByNombreForPreguntas("Ciencias");
		});
		//Verificamos que se invoquen los metodos
		verify(repository).findAll();
		verify(preguntasRepository).findByIdExam(isNull());
	}
	
	@Test
	void testArgumentMatcher() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when (preguntasRepository.findByIdExam(anyLong())).thenReturn(Datos.PREGUNTAS);
		service.findExamenByNombreForPreguntas("Ciencias");
		verify(repository).findAll();
		//verify(preguntasRepository).findByIdExam(Mockito.argThat(arg -> arg.equals(7L)));
		verify(preguntasRepository).findByIdExam(Mockito.argThat(new ArgMatcher()));
	}
}