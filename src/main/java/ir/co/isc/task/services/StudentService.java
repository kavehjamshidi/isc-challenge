package ir.co.isc.task.services;

import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.models.StudentDTO;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    List<StudentDTO> findAll();

    Optional<StudentDTO> findById(Long id);

    List<CourseDTO> findCourses(Long id);

    StudentDTO addStudent(StudentDTO student);

    Optional<StudentDTO> updateStudentById(Long id, StudentDTO student);

    Boolean deleteStudentById(Long id);

    void enrollCourse(Long courseId, Long studentId);

    void dropCourse(Long courseId, Long studentId);
}
