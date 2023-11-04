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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfessorServiceJPA implements ProfessorService {

    private final static Integer DEFAULT_PAGE_NUMBER = 0;
    private final static Integer DEFAULT_PAGE_SIZE = 10;
    private final static Integer MAX_PAGE_SIZE = 100;

    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final ProfessorMapper professorMapper;
    private final CourseMapper courseMapper;

    @Override
    public Page<ProfessorDTO> findAll(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        return professorRepository.findAll(pageRequest)
                .map(professorMapper::professorToProfessorDto);
    }

    @Override
    public ProfessorDTO findById(Long id) {
        Optional<Professor> professor = professorRepository.findById(id);

        if (professor.isEmpty()) {
            throw new NotFoundException("professor not found");
        }

        return professorMapper.professorToProfessorDto(professor.get());
    }

    @Override
    public Page<CourseDTO> findCourses(Long id, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        return courseRepository.findAllByProfessor_Id(id, pageRequest).map(courseMapper::courseToCourseDto);
    }

    @Override
    public ProfessorDTO addProfessor(ProfessorDTO professor) {
        if (professorRepository.existsByNationalId(professor.getNationalId())) {
            throw new DuplicateKeyException("duplicate nationalId");
        }

        return professorMapper.professorToProfessorDto(professorRepository.save(professorMapper.professorDtoToProfessor(professor)));
    }

    @Override
    public void updateProfessorById(Long id, ProfessorDTO professorDto) {
        Optional<Professor> professor = professorRepository.findById(id);

        if (professor.isEmpty()) {
            throw new NotFoundException("professor not found");
        }

        if (professorRepository.existsByNationalId(professorDto.getNationalId())) {
            throw new DuplicateKeyException("duplicate nationalId");
        }

        Professor foundProfessor = professor.get();
        foundProfessor.setFirstName(professorDto.getFirstName());
        foundProfessor.setLastName(professorDto.getLastName());
        foundProfessor.setNationalId(professorDto.getNationalId());

        professorRepository.save(foundProfessor);
    }

    @Override
    @Transactional
    public void deleteProfessorById(Long id) {
        if (!professorRepository.existsById(id)) {
            throw new NotFoundException("professor not found");
        }

        Optional<Professor> professor = professorRepository.findById(id);
        if (professor.isEmpty()) {
            throw new NotFoundException("professor not found");
        }
        Professor foundProfessor = professor.get();
        Set<Course> courses = foundProfessor.getCourses();
        courses.forEach(course -> {
            course.setStudents(new HashSet<>());
        });

        courseRepository.saveAll(courses);
        professorRepository.deleteById(id);
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        Integer queryPageNumber;
        Integer queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize != null && pageSize > 0 && pageSize < MAX_PAGE_SIZE) {
            queryPageSize = pageSize;
        } else {
            queryPageSize = DEFAULT_PAGE_SIZE;
        }

        Sort sort = Sort.by(Sort.Order.asc("id"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }
}
