package ir.co.isc.task.repositories;

import ir.co.isc.task.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findAll(Pageable pageable);
    Page<Course> findAllByProfessor_Id(Long professorId, Pageable pageable);
    Page<Course> findAllByStudents_Id(Long studentId, Pageable pageable);
}
