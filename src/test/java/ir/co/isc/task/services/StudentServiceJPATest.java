package ir.co.isc.task.services;

import ir.co.isc.task.domain.Course;
import ir.co.isc.task.domain.Student;
import ir.co.isc.task.exceptions.ConflictException;
import ir.co.isc.task.exceptions.NotFoundException;
import ir.co.isc.task.mappers.CourseMapper;
import ir.co.isc.task.mappers.StudentMapper;
import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.models.StudentDTO;
import ir.co.isc.task.repositories.CourseRepository;
import ir.co.isc.task.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceJPATest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private StudentServiceJPA studentService;

    @Test
    void findAll_withDefaultPaging_returnsPage() {

        // Arrange
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            students.add(new Student());
        }
        Page<Student> response = new PageImpl<>(students);

        when(studentRepository.findAll(any(PageRequest.class)))
                .thenReturn(response);

        // Act
        Page<StudentDTO> result = studentService.findAll(null, null);

        // Assert
        assertEquals(10, result.getSize());
        verify(studentRepository).findAll(PageRequest.of(0, 10, Sort.by("id")));

    }

    @Test
    void findAll_withProvidedPaging_returnsPage() {

        // Arrange
        int page = 1;
        int size = 20;
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            students.add(new Student());
        }
        Page<Student> response = new PageImpl<>(students);

        when(studentRepository.findAll(any(PageRequest.class)))
                .thenReturn(response);

        // Act
        Page<StudentDTO> result = studentService.findAll(page, size);

        // Assert
        assertEquals(size, result.getSize());
        verify(studentRepository).findAll(PageRequest.of(page - 1, size, Sort.by("id")));

    }

    @Test
    void findById_whenStudentExists_returnsDTO() {
        // Arrange
        long id = 1L;
        Student student = new Student();
        StudentDTO dto = new StudentDTO();

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        when(studentMapper.studentToStudentDto(student)).thenReturn(dto);

        // Act
        StudentDTO result = studentService.findById(id);

        // Assert
        assertEquals(dto, result);
    }

    @Test
    void findById_whenStudentNotExists_throwsException() {
        // Arrange
        long id = 1L;

        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> {
            studentService.findById(id);
        });
    }

    @Test
    void findCourses_returnsCoursesPage() {

        // Arrange
        long studentId = 1L;
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            courses.add(new Course());
        }
        Page<Course> response = new PageImpl<>(courses);

        when(courseRepository.findAllByStudents_Id(eq(studentId), any(PageRequest.class)))
                .thenReturn(response);

        // Act
        Page<CourseDTO> result = studentService.findCourses(studentId, null, null);

        // Assert
        assertEquals(10, result.getSize());
        verify(courseRepository).findAllByStudents_Id(studentId, PageRequest.of(0, 10, Sort.by("id")));

    }

    @Test
    void addStudent_withUniqueId_savesAndReturnsDTO() {
        // Arrange
        StudentDTO dto = new StudentDTO();
        Student student = new Student();

        when(studentRepository.existsByNationalId(dto.getNationalId())).thenReturn(false);
        when(studentMapper.studentDtoToStudent(dto)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.studentToStudentDto(student)).thenReturn(dto);

        // Act
        StudentDTO result = studentService.addStudent(dto);

        // Assert
        assertEquals(dto, result);
    }

    @Test
    void addStudent_withDuplicateId_throwsException() {
        // Arrange
        StudentDTO dto = new StudentDTO();

        when(studentRepository.existsByNationalId(dto.getNationalId())).thenReturn(true);

        // Act + Assert
        assertThrows(DuplicateKeyException.class, () -> {
            studentService.addStudent(dto);
        });
    }

    @Test
    void updateStudent_withValidData_updatesAndReturnsDTO() {

        // Arrange
        long id = 1L;
        StudentDTO dto = new StudentDTO();
        dto.setFirstName("first name");
        dto.setLastName("last name");
        dto.setNationalId("1234567891");
        Student student = new Student();

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        when(studentRepository.existsByNationalId(dto.getNationalId())).thenReturn(false);

        // Act
        studentService.updateStudentById(id, dto);

        // Assert
        assertEquals(dto.getFirstName(), student.getFirstName());
        assertEquals(dto.getLastName(), student.getLastName());
        assertEquals(dto.getNationalId(), student.getNationalId());
        verify(studentRepository).save(student);

    }

    @Test
    void updateStudent_whenNotFound_throwsException() {

        // Arrange
        long id = 1L;
        StudentDTO dto = new StudentDTO();

        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> {
            studentService.updateStudentById(id, dto);
        });

    }

    @Test
    void updateStudent_withDuplicateId_throwsException() {

        // Arrange
        long id = 1L;
        StudentDTO dto = new StudentDTO();
        Student student = new Student();

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        when(studentRepository.existsByNationalId(dto.getNationalId())).thenReturn(true);

        // Act + Assert
        assertThrows(DuplicateKeyException.class, () -> {
            studentService.updateStudentById(id, dto);
        });

    }

    @Test
    void deleteStudent_removesCoursesAndDeletesStudent() {
        // Arrange
        long id = 1L;
        Student student = new Student();
        Course course1 = new Course();
        course1.setId(1L);
        Course course2 = new Course();
        course2.setId(2L);

        student.setCourses(Set.of(course1, course2));

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        // Act
        studentService.deleteStudentById(id);

        // Assert
        assertEquals(0, course1.getStudents().size());
        assertEquals(0, course2.getStudents().size());
        verify(courseRepository).saveAll(Set.of(course1, course2));
        verify(studentRepository).deleteById(id);
    }

    @Test
    void enrollCourse_withValidData_enrollsStudent() {
        // Arrange
        long studentId = 1L;
        long courseId = 1L;
        Student student = new Student();
        Course course = new Course();
        course.setCapacity(10);
        course.setId(courseId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        studentService.enrollCourse(studentId, courseId);

        // Assert
        assertEquals(1, student.getCourses().size());
        verify(studentRepository).save(student);
    }

    @Test
    void enrollCourse_whenAlreadyEnrolled_throwsException() {
        // Arrange
        long studentId = 1L;
        long courseId = 1L;
        Student student = new Student();
        Course course = new Course();
        course.setCapacity(10);
        course.setId(courseId);
        student.addCourse(course);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act + Assert
        assertThrows(ConflictException.class, () -> {
            studentService.enrollCourse(studentId, courseId);
        });
    }

    @Test
    void enrollCourse_whenFull_throwsException() {
        // Arrange
        long studentId = 1L;
        long courseId = 1L;
        Student student = new Student();
        Course course = new Course();
        course.setCapacity(1);
        course.setId(courseId);
        Student otherStudent = new Student();
        otherStudent.setId(2L);
        course.addStudent(otherStudent);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act + Assert
        assertThrows(ConflictException.class, () -> {
            studentService.enrollCourse(studentId, courseId);
        });
    }

    @Test
    void dropCourse_withValidData_dropsCourse() {
        // Arrange
        long studentId = 1L;
        long courseId = 1L;
        Student student = new Student();
        student.setId(studentId);
        Course course = new Course();
        course.setId(courseId);
        student.addCourse(course);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        studentService.dropCourse(studentId, courseId);

        // Assert
        assertEquals(0, student.getCourses().size());
        verify(studentRepository).save(student);
    }

    @Test
    void dropCourse_whenNotEnrolled_throwsException() {
        // Arrange
        long studentId = 1L;
        long courseId = 1L;
        Student student = new Student();
        Course course = new Course();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act + Assert
        assertThrows(ConflictException.class, () -> {
            studentService.dropCourse(studentId, courseId);
        });
    }

}
