package ir.co.isc.task.services;

import ir.co.isc.task.mappers.CourseMapper;
import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceJPA implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public List<CourseDTO> findAll() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::courseToCourseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CourseDTO> findById(Long id) {
        return Optional.ofNullable(courseMapper.courseToCourseDto(courseRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public CourseDTO addCourse(CourseDTO course) {
        return courseMapper.courseToCourseDto(courseRepository.save(courseMapper.courseDtoToCourse(course)));
    }

    @Override
    public Optional<CourseDTO> updateCourseById(Long id, CourseDTO courseDTO) {
        AtomicReference<Optional<CourseDTO>> atomicReference = new AtomicReference<>();

        courseRepository.findById(id).ifPresentOrElse(foundCourse -> {
            foundCourse.setTitle(courseDTO.getTitle());
            foundCourse.setCapacity(courseDTO.getCapacity());
            foundCourse.setUpdatedAt(LocalDateTime.now());

            atomicReference.set(Optional.of(courseMapper
                    .courseToCourseDto(courseRepository.save(foundCourse))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteCourseById(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
