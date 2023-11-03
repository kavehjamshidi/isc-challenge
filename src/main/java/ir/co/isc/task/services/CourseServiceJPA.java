package ir.co.isc.task.services;

import ir.co.isc.task.domain.Course;
import ir.co.isc.task.domain.Professor;
import ir.co.isc.task.exceptions.NotFoundException;
import ir.co.isc.task.mappers.CourseMapper;
import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.repositories.CourseRepository;
import ir.co.isc.task.repositories.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceJPA implements CourseService {

    private final static Integer DEFAULT_PAGE_NUMBER = 0;
    private final static Integer DEFAULT_PAGE_SIZE = 10;
    private final static Integer MAX_PAGE_SIZE = 100;

    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public Page<CourseDTO> findAll(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        return courseRepository.findAll(pageRequest).map(courseMapper::courseToCourseDto);
    }

    @Override
    public CourseDTO findById(Long id) {
        Optional<Course> course = courseRepository.findById(id);

        if (course.isEmpty()) {
            throw new NotFoundException("course not found");
        }

        return courseMapper.courseToCourseDto(course.get());
    }

    @Override
    public CourseDTO addCourse(CourseDTO courseDto) {
        Course course = courseMapper.courseDtoToCourse(courseDto);

        Optional<Professor> professor = professorRepository.findById(courseDto.getProfessorId());
        if (professor.isEmpty()) {
            throw new NotFoundException("professor not found");
        }

        Professor foundProfessor = professor.get();
        course.setProfessor(foundProfessor);

        return courseMapper.courseToCourseDto(courseRepository.save(course));
    }

    @Override
    public void updateCourseById(Long id, CourseDTO courseDto) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isEmpty()) {
            throw new NotFoundException("course not found");
        }

        Course foundCourse = course.get();
        foundCourse.setTitle(courseDto.getTitle());
        foundCourse.setCapacity(courseDto.getCapacity());

        Optional<Professor> professor = professorRepository.findById(courseDto.getProfessorId());
        if (professor.isEmpty()) {
            throw new NotFoundException("professor not found");
        }

        Professor foundProfessor = professor.get();
        foundCourse.setProfessor(foundProfessor);

        courseRepository.save(foundCourse);
    }

    @Override
    public void deleteCourseById(Long id) {
        if (courseRepository.existsById(id)) {
            throw new NotFoundException("course not found");
        }
        courseRepository.deleteById(id);
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

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
