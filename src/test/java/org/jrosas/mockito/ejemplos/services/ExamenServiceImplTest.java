package org.jrosas.mockito.ejemplos.services;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jrosas.mockito.ejemplos.Datos;
import org.jrosas.mockito.ejemplos.models.Examen;
import org.jrosas.mockito.ejemplos.repositories.ExamenRepoImpl;
import org.jrosas.mockito.ejemplos.repositories.IExamenRepository;
import org.jrosas.mockito.ejemplos.repositories.IPreguntasRepository;
import org.jrosas.mockito.ejemplos.repositories.PreguntasRepoImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*; //De esta forma no necesito poner la clase antes del metodo estatico

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {
	//Concrete implementation
	@Mock
	ExamenRepoImpl examenRepo;
	//abstract impl
	@Mock
	IExamenRepository repository;
	@Mock
	IPreguntasRepository preguntasRepository;
	
	@InjectMocks //Inyecta los @Mock a la clase implementadora
	ExamenServiceImpl service;
	
	@Captor
	ArgumentCaptor<Long> captor;
	
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
		//Aqui se invocan los metodos con un examen que no existe
		Examen examen = service.findExamenByNombreForPreguntas("Prueba");
		assertNull(examen);
		 verify(repository).findAll();
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
	
	@Test
	void testArgumentCaptor() {
		//When solo se usa cuando el metodo devuelve un valor
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when (preguntasRepository.findByIdExam(anyLong())).thenReturn(Datos.PREGUNTAS);
		service.findExamenByNombreForPreguntas("Ciencias");
		//Captura el tipo de argumento que le estamos pasando
		//Se reemplaza con una anotacion al principio de la clase
		//ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
		verify(preguntasRepository).findByIdExam(captor.capture());
		assertEquals(7L, captor.getValue());
		
		
	}
	
	//Throw se usa cuando el metodo es void (no regresa nada) en vez del when
	@Test 
	void doThrow() {
		//Creamos examen, seteamos preguntas, guardamos examen y esperamos excepcion
		Examen examen = Datos.EXAMENES.get(0);
		examen.setPreguntas(Datos.PREGUNTAS);
		Mockito.doThrow(IllegalArgumentException.class).when(preguntasRepository).saveSomeQuestions(anyList());
		assertThrows(IllegalArgumentException.class, () -> {
			service.save(examen);
		});
	}
	
	@Test
	void testDoAnswer() {
		
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		doAnswer(invocation -> {
			Long id = invocation.getArgument(0);
			return id ==5L? Datos.PREGUNTAS:null;
			
		}).when(preguntasRepository).findByIdExam(anyLong()); //when pase esto se dispara el doAnswer
		
		Examen examen = service.findExamenByNombreForPreguntas("Matematicas");
		
		assertEquals(5L, examen.getId());
		assertEquals("Matematicas", examen.getNombre());
	}

	//doCallRealMethod() manda a llamar al metodo real y no al mock3
	//usamos clase concreta ExamenRepoImpl
	@Test
	void testDoCallRealMethod(){
		//Mocking method will return only examen 0: Matematicas
		when(examenRepo.findAll()).thenReturn(Arrays.asList(Datos.EXAMENES.get(0)));
		//Calling actual method: actual method return Lenguajes 6L
		doCallRealMethod().when(examenRepo).findById(anyLong());
		List<Examen> list = examenRepo.findAll();
		Examen examen = examenRepo.findById(anyLong());
		//Mocking data
		assertEquals(1,list.size());
		assertEquals("Matematicas",list.get(0).getNombre());
		//Not mock, current method class impl
		assertEquals(6L, examen.getId());
		assertEquals("Lenguajes", examen.getNombre());

	}

	/////SPY hibrido entre mock y objeto real, no usar mucho solo en metodos de terceros
	//Spy va al metodo real y tu puedes especificar el metodo que quieres mockear.
	///Mock 100% simulado (puede ser interfaz, damos implementacion falsa mock), spy need an impl because use actual methods

	@Test
	void testSpy(){

		List<String> preguntas = Arrays.asList("Test");
		IExamenRepository examenRepo = spy(ExamenRepoImpl.class);
		IPreguntasRepository preguntasRepository = spy(PreguntasRepoImpl.class);
		IExamenService examenService = new ExamenServiceImpl(examenRepo,preguntasRepository);
		//We only mock this method, when call actual method and mock it is weird so we change it to doReturn
		doReturn(preguntas).when(preguntasRepository).findByIdExam(anyLong());
		//when(preguntasRepository.findByIdExam(anyLong())).thenReturn(preguntas);
		Examen examen = examenService.findExamenByNombreForPreguntas("Matematicas");
		assertEquals(5L, examen.getId());
		assertEquals("Matematicas", examen.getNombre());
		//real method return null -> preguntasRepository
		//assertNull(examen.getPreguntas());

		//Simulated spy
		assertNotNull(examen.getPreguntas());
		assertEquals(1,examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("Test"));
		verify(examenRepo).findAll();

	}

	@Test
	void testOrderInvocation(){

		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		service.findExamenByNombreForPreguntas("Matematicas");
		service.findExamenByNombreForPreguntas("Lenguajes");
		InOrder inOrder = inOrder(preguntasRepository);
		//First we return matematicas and then Lenguajes
		inOrder.verify(preguntasRepository).findByIdExam(5L);
		inOrder.verify(preguntasRepository).findByIdExam(6L);


	}



	
	
}