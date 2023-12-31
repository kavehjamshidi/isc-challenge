package ir.co.isc.task.repositories;

import ir.co.isc.task.domain.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Boolean existsByNationalId(String nationalId);
    Page<Professor> findAll(Pageable pageable);
}
