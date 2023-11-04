package ir.co.isc.task.services;

import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.models.StudentDTO;
import org.springframework.data.domain.Page;

public interface StudentService {

    Page<StudentDTO> findAll(Integer pageNumber, Integer pageSize);
    StudentDTO findById(Long id);
    Page<CourseDTO> findCourses(Long id, Integer pageNumber, Integer pageSize);
    StudentDTO addStudent(StudentDTO studentDto);
    void updateStudentById(Long id, StudentDTO studentDto);
    void deleteStudentById(Long id);
    void enrollCourse(Long studentId, Long courseId);
    void dropCourse(Long studentId, Long courseId);
}
