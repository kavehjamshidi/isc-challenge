package ir.co.isc.task.services;

import ir.co.isc.task.models.CourseDTO;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<CourseDTO> findAll();

    Optional<CourseDTO> findById(Long id);

    CourseDTO addCourse(CourseDTO course);

    Optional<CourseDTO> updateCourseById(Long id, CourseDTO courseDTO);

    Boolean deleteCourseById(Long id);
}

