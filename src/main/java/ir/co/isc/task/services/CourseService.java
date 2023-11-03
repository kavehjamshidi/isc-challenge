package ir.co.isc.task.services;

import ir.co.isc.task.models.CourseDTO;
import org.springframework.data.domain.Page;

public interface CourseService {

    Page<CourseDTO> findAll(Integer pageNumber, Integer pageSize);

    CourseDTO findById(Long id);

    CourseDTO addCourse(CourseDTO courseDto);

    void updateCourseById(Long id, CourseDTO courseDto);

    void deleteCourseById(Long id);
}

