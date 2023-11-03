package ir.co.isc.task.services;

import ir.co.isc.task.mappers.CourseMapper;
import ir.co.isc.task.mappers.StudentMapper;
import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.models.StudentDTO;
import ir.co.isc.task.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceJPA implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;

    @Override
    public List<StudentDTO> findAll() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::studentToStudentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<StudentDTO> findById(Long id) {
        return Optional.ofNullable(studentMapper.studentToStudentDto(studentRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public List<CourseDTO> findCourses(Long id) {
        return null;
    }

    @Override
    public StudentDTO addStudent(StudentDTO student) {
        if (studentRepository.existsByNationalId(student.getNationalId())){
            throw new DuplicateKeyException("duplicate nationalId");
        }

        return studentMapper.studentToStudentDto(studentRepository.save(studentMapper.studentDtoToStudent(student)));
    }

    @Override
    public Optional<StudentDTO> updateStudentById(Long id, StudentDTO student) {
        AtomicReference<Optional<StudentDTO>> atomicReference = new AtomicReference<>();

        studentRepository.findById(id).ifPresentOrElse(foundStudent -> {
            if (studentRepository.existsByNationalId(student.getNationalId())){
                throw new DuplicateKeyException("duplicate nationalId");
            }

            foundStudent.setFirstName(student.getFirstName());
            foundStudent.setLastName(student.getLastName());
            foundStudent.setNationalId(student.getNationalId());
            foundStudent.setUpdatedAt(LocalDateTime.now());

            atomicReference.set(Optional.of(studentMapper
                    .studentToStudentDto(studentRepository.save(foundStudent))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteStudentById(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void enrollCourse(Long courseId, Long studentId) {

    }

    @Override
    public void dropCourse(Long courseId, Long studentId) {

    }
}
