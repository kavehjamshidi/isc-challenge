package ir.co.isc.task.repositories;

import ir.co.isc.task.domain.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {
}
