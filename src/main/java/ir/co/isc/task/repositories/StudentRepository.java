package ir.co.isc.task.repositories;

import ir.co.isc.task.domain.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
}
