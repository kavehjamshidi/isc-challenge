package ir.co.isc.task.mappers;

import ir.co.isc.task.domain.Course;
import ir.co.isc.task.models.CourseDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CourseMapper {
    Course courseDtoToCourse(CourseDTO courseDTO);

    CourseDTO courseToCourseDto(Course course);
}
