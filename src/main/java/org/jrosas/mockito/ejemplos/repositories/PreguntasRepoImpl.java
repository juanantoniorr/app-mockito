package org.jrosas.mockito.ejemplos.repositories;

import java.util.List;

public class PreguntasRepoImpl implements IPreguntasRepository{
    @Override
    public List<String> findByIdExam(Long id) {
        return null;
    }

    @Override
    public void saveSomeQuestions(List<String> preguntas) {

    }
}
