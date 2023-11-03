package ir.co.isc.task.repositories;

import ir.co.isc.task.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Boolean existsByNationalId(String nationalId);
}
