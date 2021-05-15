package org.jrosas.mockito.ejemplos.repositories;

import java.util.List;

public interface IPreguntasRepository {
	
	List <String> findByIdExam(Long id);

}
