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
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceJPA implements StudentService {

    private final static Integer DEFAULT_PAGE_NUMBER = 0;
    private final static Integer DEFAULT_PAGE_SIZE = 10;
    private final static Integer MAX_PAGE_SIZE = 100;

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;

    @Override
    public Page<StudentDTO> findAll(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        return studentRepository.findAll(pageRequest).map(studentMapper::studentToStudentDto);
    }

    @Override
    public StudentDTO findById(Long id) {
        Optional<Student> student = studentRepository.findById(id);

        if (student.isEmpty()) {
            throw new NotFoundException("student not found");
        }

        return studentMapper.studentToStudentDto(student.get());
    }

    @Override
    public Page<CourseDTO> findCourses(Long id, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        return courseRepository.findAllByStudents_Id(id, pageRequest).map(courseMapper::courseToCourseDto);
    }

    @Override
    public StudentDTO addStudent(StudentDTO studentDto) {
        if (studentRepository.existsByNationalId(studentDto.getNationalId())) {
            throw new DuplicateKeyException("duplicate nationalId");
        }

        return studentMapper.studentToStudentDto(studentRepository.save(studentMapper.studentDtoToStudent(studentDto)));
    }

    @Override
    public void updateStudentById(Long id, StudentDTO studentDto) {
        Optional<Student> student = studentRepository.findById(id);

        if (student.isEmpty()) {
            throw new NotFoundException("student not found");
        }

        if (studentRepository.existsByNationalId(studentDto.getNationalId())) {
            throw new DuplicateKeyException("duplicate nationalId");
        }

        Student foundStudent = student.get();
        foundStudent.setFirstName(studentDto.getFirstName());
        foundStudent.setLastName(studentDto.getLastName());
        foundStudent.setNationalId(studentDto.getNationalId());

        studentRepository.save(foundStudent);
    }

    @Override
    public void deleteStudentById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new NotFoundException("student not found");
        }
        studentRepository.deleteById(id);
    }

    @Override
    public void enrollCourse(Long studentId, Long courseId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("student not found");
        }
        Student foundStudent = student.get();

        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()){
            throw new NotFoundException("course not found");
        }
        Course foundCourse = course.get();

        if (foundStudent.getCourses().contains(foundCourse)) {
            throw new ConflictException("already enrolled");
        }

        foundStudent.getCourses().add(foundCourse);

        studentRepository.save(foundStudent);
    }

    @Override
    public void dropCourse(Long studentId, Long courseId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("student not found");
        }
        Student foundStudent = student.get();

        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()){
            throw new NotFoundException("course not found");
        }
        Course foundCourse = course.get();

        if (!foundStudent.getCourses().contains(foundCourse)) {
            throw new ConflictException("not enrolled");
        }

        foundStudent.getCourses().remove(foundCourse);

        studentRepository.save(foundStudent);
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
