package ir.co.isc.task.services;

import ir.co.isc.task.domain.Course;
import ir.co.isc.task.domain.Professor;
import ir.co.isc.task.exceptions.NotFoundException;
import ir.co.isc.task.mappers.CourseMapper;
import ir.co.isc.task.mappers.ProfessorMapper;
import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.models.ProfessorDTO;
import ir.co.isc.task.repositories.CourseRepository;
import ir.co.isc.task.repositories.ProfessorRepository;
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
class ProfessorServiceJPATest {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ProfessorMapper professorMapper;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private ProfessorServiceJPA professorService;

    @Test
    void findAll_withDefaultPaging_returnsPage() {

        // Arrange
        List<Professor> professors = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            professors.add(new Professor());
        }
        Page<Professor> response = new PageImpl<>(professors);

        when(professorRepository.findAll(any(PageRequest.class)))
                .thenReturn(response);

        // Act
        Page<ProfessorDTO> result = professorService.findAll(null, null);

        // Assert
        assertEquals(10, result.getSize());
        verify(professorRepository).findAll(PageRequest.of(0, 10, Sort.by("id")));

    }

    @Test
    void findAll_withProvidedPaging_returnsPage() {

        // Arrange
        int page = 1;
        int size = 20;
        List<Professor> professors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            professors.add(new Professor());
        }
        Page<Professor> response = new PageImpl<>(professors);

        when(professorRepository.findAll(any(PageRequest.class)))
                .thenReturn(response);

        // Act
        Page<ProfessorDTO> result = professorService.findAll(page, size);

        // Assert
        assertEquals(size, result.getSize());
        verify(professorRepository).findAll(PageRequest.of(page-1, size, Sort.by("id")));

    }

    @Test
    void findById_whenProfessorExists_returnsDTO() {

        // Arrange
        long id = 1L;
        Professor professor = new Professor();
        ProfessorDTO dto = new ProfessorDTO();

        when(professorRepository.findById(id))
                .thenReturn(Optional.of(professor));
        when(professorMapper.professorToProfessorDto(professor))
                .thenReturn(dto);

        // Act
        ProfessorDTO result = professorService.findById(id);

        // Assert
        assertEquals(dto, result);

    }

    @Test
    void findById_whenProfessorNotExists_throwsException() {

        // Arrange
        long id = 1L;

        when(professorRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> {
            professorService.findById(id);
        });

    }

    @Test
    void findCourses_withDefaultPaging_returnsPage() {

        // Arrange
        long professorId = 1L;
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            courses.add(new Course());
        }
        Page<Course> response = new PageImpl<>(courses);

        when(courseRepository.findAllByProfessor_Id(eq(professorId), any(PageRequest.class)))
                .thenReturn(response);

        // Act
        Page<CourseDTO> result = professorService.findCourses(professorId, null, null);

        // Assert
        assertEquals(10, result.getSize());
        verify(courseRepository).findAllByProfessor_Id(professorId, PageRequest.of(0, 10, Sort.by("id")));

    }

    @Test
    void findCourses_withProvidedPaging_returnsPage() {

        // Arrange
        long professorId = 1L;
        int page = 1;
        int size = 20;
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < size; i++){
            courses.add(new Course());
        }
        Page<Course> response = new PageImpl<>(courses);

        when(courseRepository.findAllByProfessor_Id(eq(professorId), any(PageRequest.class)))
                .thenReturn(response);

        // Act
        Page<CourseDTO> result = professorService.findCourses(professorId, page, size);

        // Assert
        assertEquals(size, result.getSize());
        verify(courseRepository).findAllByProfessor_Id(professorId, PageRequest.of(page-1, size, Sort.by("id")));

    }

    @Test
    void addProfessor_withUniqueId_savesAndReturnsDTO() {

        // Arrange
        ProfessorDTO dto = new ProfessorDTO();
        Professor professor = new Professor();

        when(professorRepository.existsByNationalId(dto.getNationalId())).thenReturn(false);
        when(professorMapper.professorDtoToProfessor(dto)).thenReturn(professor);
        when(professorRepository.save(professor)).thenReturn(professor);
        when(professorMapper.professorToProfessorDto(professor)).thenReturn(dto);

        // Act
        ProfessorDTO result = professorService.addProfessor(dto);

        // Assert
        assertEquals(dto, result);

    }

    @Test
    void addProfessor_withDuplicateId_throwsException() {

        // Arrange
        ProfessorDTO dto = new ProfessorDTO();

        when(professorRepository.existsByNationalId(dto.getNationalId())).thenReturn(true);

        // Act + Assert
        assertThrows(DuplicateKeyException.class, () -> {
            professorService.addProfessor(dto);
        });

    }

    @Test
    void updateProfessor_withValidData_updatesAndReturnsDTO() {

        // Arrange
        long id = 1L;
        ProfessorDTO dto = new ProfessorDTO();
        dto.setFirstName("first name");
        dto.setLastName("last name");
        dto.setNationalId("1234567891");

        Professor professor = new Professor();

        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));
        when(professorRepository.existsByNationalId(dto.getNationalId())).thenReturn(false);

        // Act
        professorService.updateProfessorById(id, dto);

        // Assert
        assertEquals(dto.getFirstName(), professor.getFirstName());
        assertEquals(dto.getLastName(), professor.getLastName());
        assertEquals(dto.getNationalId(), professor.getNationalId());
        verify(professorRepository).save(professor);

    }

    @Test
    void updateProfessor_whenProfessorNotFound_throwsException() {

        // Arrange
        long id = 1L;
        ProfessorDTO dto = new ProfessorDTO();

        when(professorRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> {
            professorService.updateProfessorById(id, dto);
        });

    }

    @Test
    void updateProfessor_withDuplicateId_throwsException() {

        // Arrange
        long id = 1L;
        ProfessorDTO dto = new ProfessorDTO();
        Professor professor = new Professor();

        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));
        when(professorRepository.existsByNationalId(dto.getNationalId())).thenReturn(true);

        // Act + Assert
        assertThrows(DuplicateKeyException.class, () -> {
            professorService.updateProfessorById(id, dto);
        });

    }

    @Test
    void deleteProfessor_removesCoursesAndDeletes() {

        // Arrange
        long id = 1L;
        Professor professor = new Professor();
        Course course1 = new Course();
        course1.setId(1L);
        Course course2 = new Course();
        course2.setId(2L);
        professor.setCourses(Set.of(course1, course2));

        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));

        // Act
        professorService.deleteProfessorById(id);

        // Assert
        assertEquals(0, course1.getStudents().size());
        assertEquals(0, course2.getStudents().size());
        verify(courseRepository).saveAll(Set.of(course1, course2));
        verify(professorRepository).deleteById(id);

    }

    @Test
    void deleteProfessor_whenNotFound_throwsException() {

        // Arrange
        long id = 1L;

        when(professorRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> {
            professorService.deleteProfessorById(id);
        });

    }

}