package ir.co.isc.task.services;

import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.models.ProfessorDTO;
import org.springframework.data.domain.Page;

public interface ProfessorService {

    Page<ProfessorDTO> findAll(Integer pageNumber, Integer pageSize);
    ProfessorDTO findById(Long id);
    Page<CourseDTO> findCourses(Long id, Integer pageNumber, Integer pageSize);
    ProfessorDTO addProfessor(ProfessorDTO professorDto);
    void updateProfessorById(Long id, ProfessorDTO professorDto);
    void deleteProfessorById(Long id);
}
