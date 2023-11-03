package ir.co.isc.task.mappers;

import ir.co.isc.task.domain.Professor;
import ir.co.isc.task.models.ProfessorDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ProfessorMapper {
    Professor professorDtoToProfessor(ProfessorDTO professorDto);

    ProfessorDTO professorToProfessorDto(Professor professor);
}
