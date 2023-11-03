package ir.co.isc.task.mappers;

import ir.co.isc.task.domain.Student;
import ir.co.isc.task.models.StudentDTO;
import org.mapstruct.Mapper;

@Mapper
public interface StudentMapper {
    Student studentDtoToStudent(StudentDTO studentDTO);

    StudentDTO studentToStudentDto(Student student);
}
