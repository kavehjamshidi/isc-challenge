package ir.co.isc.task.repositories;

import ir.co.isc.task.domain.Professor;
import org.springframework.data.repository.CrudRepository;

public interface ProfessorRepository extends CrudRepository<Professor, Long> {
}
