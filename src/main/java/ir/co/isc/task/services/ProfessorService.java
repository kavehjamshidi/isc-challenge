package ir.co.isc.task.services;

import ir.co.isc.task.models.ProfessorDTO;

public interface ProfessorService {

    Iterable<ProfessorDTO> findAll();
}
