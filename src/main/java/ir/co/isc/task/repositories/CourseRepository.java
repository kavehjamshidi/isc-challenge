package ir.co.isc.task.repositories;

import ir.co.isc.task.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
