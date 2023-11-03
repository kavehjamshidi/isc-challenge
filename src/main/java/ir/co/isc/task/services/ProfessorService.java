package ir.co.isc.task.services;

import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.models.ProfessorDTO;

import java.util.List;
import java.util.Optional;

public interface ProfessorService {

    List<ProfessorDTO> findAll();

    Optional<ProfessorDTO> findById(Long id);

    List<CourseDTO> findCourses(Long id);

    ProfessorDTO addProfessor(ProfessorDTO professor);

    Optional<ProfessorDTO> updateProfessorById(Long id, ProfessorDTO professor);

    Boolean deleteProfessorById(Long id);
}
