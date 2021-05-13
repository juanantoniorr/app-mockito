package org.jrosas.mockito.ejemplos.services;

import static org.junit.jupiter.api.Assertions.*;

import org.jrosas.mockito.ejemplos.models.Examen;
import org.jrosas.mockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.jrosas.mockito.ejemplos.repositories.IExamenRepository;
import org.junit.jupiter.api.Test;

class ExamenServiceImplTest {
	
	@Test
	void findExamenByNombre() {
		
		IExamenRepository repository = new ExamenRepositoryImpl();
		IExamenService service = new ExamenServiceImpl(repository);
		Examen examen = service.findByName("Ciencias");
		assertNotNull(examen);
		assertEquals(7, examen.getId());
		assertEquals("Ciencias", examen.getNombre());
	}

}