package ir.co.isc.task.mappers;

import ir.co.isc.task.domain.Course;
import ir.co.isc.task.models.CourseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CourseMapper {
    Course courseDtoToCourse(CourseDTO courseDTO);

    @Mapping(target = "professorId", source = "professor.id")
    CourseDTO courseToCourseDto(Course course);
}
