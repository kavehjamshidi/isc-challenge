package ir.co.isc.task.services;

import ir.co.isc.task.domain.Course;
import ir.co.isc.task.domain.Professor;
import ir.co.isc.task.domain.Student;
import ir.co.isc.task.exceptions.NotFoundException;
import ir.co.isc.task.mappers.CourseMapper;
import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.repositories.CourseRepository;
import ir.co.isc.task.repositories.ProfessorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceJPATest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceJPA courseService;

    @Test
    void findAll_withDefaultPaging_returnsPage() {

        // Arrange
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            courses.add(new Course());
        }

        int page = 0;
        int size = 10;

        List<Course> pageCourses = courses.subList(page*size, page*size + size);
        Page<Course> pagedResponse = new PageImpl<>(pageCourses);

        when(courseRepository.findAll(any(PageRequest.class)))
                .thenReturn(pagedResponse);

        // Act
        Page<CourseDTO> result = courseService.findAll(null, null);

        // Assert
        assertEquals(10, result.getSize());
        verify(courseRepository).findAll(PageRequest.of(0, 10, Sort.by("id")));

    }

    @Test
    void findAll_withProvidedPaging_returnsPage() {

        // Arrange
        int pageNumber = 2;
        int pageSize = 5;
        List<Course> courses = new ArrayList<>();

        for (int i = 0; i < 15; i++){
            courses.add(new Course());
        }

        List<Course> pageCourses = courses.subList((pageNumber-1)*pageSize, (pageNumber-1)*pageSize + pageSize);
        Page<Course> pagedResponse = new PageImpl<>(pageCourses);

        when(courseRepository.findAll(any(PageRequest.class)))
                .thenReturn(pagedResponse);

        // Act
        Page<CourseDTO> result = courseService.findAll(pageNumber, pageSize);

        // Assert
        assertEquals(pageSize, result.getSize());
        verify(courseRepository).findAll(PageRequest.of(pageNumber - 1, pageSize, Sort.by("id")));

    }

    @Test
    void findById_whenCourseExists_returnsDTO() {

        // Arrange
        long courseId = 1L;
        Course course = new Course();
        CourseDTO dto = new CourseDTO();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.courseToCourseDto(course)).thenReturn(dto);

        // Act
        CourseDTO result = courseService.findById(courseId);

        // Assert
        assertEquals(dto, result);

    }

    @Test
    void findById_whenCourseNotExists_throwsException() {

        // Arrange
        long courseId = 1L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> {
            courseService.findById(courseId);
        });

    }

    @Test
    void addCourse_withValidData_savesAndReturnsDTO() {

        // Arrange
        CourseDTO dto = new CourseDTO();
        dto.setProfessorId(1L);
        Course course = new Course();
        Professor professor = new Professor();

        when(professorRepository.findById(dto.getProfessorId())).thenReturn(Optional.of(professor));
        when(courseMapper.courseDtoToCourse(dto)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.courseToCourseDto(course)).thenReturn(dto);

        // Act
        CourseDTO result = courseService.addCourse(dto);

        // Assert
        assertEquals(dto, result);
        verify(professorRepository).findById(dto.getProfessorId());
        verify(courseMapper).courseDtoToCourse(dto);
        verify(courseRepository).save(course);

    }

    @Test
    void addCourse_whenProfessorNotFound_throwsException() {

        // Arrange
        CourseDTO dto = new CourseDTO();
        dto.setProfessorId(1L);

        when(professorRepository.findById(dto.getProfessorId())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> {
            courseService.addCourse(dto);
        });

    }

    @Test
    void updateCourse_withValidData_savesCourse() {

        // Arrange
        long courseId = 1L;
        CourseDTO dto = new CourseDTO();
        Course course = new Course();
        Professor professor = new Professor();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(professorRepository.findById(dto.getProfessorId())).thenReturn(Optional.of(professor));

        // Act
//        courseService.updateCourseById(courseId, dto);

        // Assert
        assertDoesNotThrow(()->{
            courseService.updateCourseById(courseId, dto);
        });

    }

    @Test
    void updateCourse_whenCourseNotFound_throwsException() {

        // Arrange
        long courseId = 1L;
        CourseDTO dto = new CourseDTO();

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> {
            courseService.updateCourseById(courseId, dto);
        });

    }

    @Test
    void updateCourse_whenProfessorNotFound_throwsException() {

        // Arrange
        long courseId = 1L;
        CourseDTO dto = new CourseDTO();
        Course course = new Course();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(professorRepository.findById(dto.getProfessorId())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> {
            courseService.updateCourseById(courseId, dto);
        });

    }

    @Test
    void deleteCourse_removesStudentsAndDeletes() {

        // Arrange
        long courseId = 1L;
        Course course = new Course();
        course.setStudents(Set.of(new Student()));

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        courseService.deleteCourseById(courseId);

        // Assert
        assertEquals(0, course.getStudents().size());
        verify(courseRepository).deleteById(courseId);

    }

    @Test
    void deleteCourse_whenNotFound_throwsException() {

        // Arrange
        long courseId = 1L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> {
            courseService.deleteCourseById(courseId);
        });

    }

}