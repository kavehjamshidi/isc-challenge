package ir.co.isc.task.services;

import ir.co.isc.task.domain.Professor;

public interface ProfessorService {

    Iterable<Professor> findAll();
}
