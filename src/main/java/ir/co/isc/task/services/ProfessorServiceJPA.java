package ir.co.isc.task.services;

import ir.co.isc.task.mappers.CourseMapper;
import ir.co.isc.task.mappers.ProfessorMapper;
import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.models.ProfessorDTO;
import ir.co.isc.task.repositories.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorServiceJPA implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final ProfessorMapper professorMapper;
    private final CourseMapper courseMapper;

    @Override
    public List<ProfessorDTO> findAll() {
        return professorRepository.findAll()
                .stream()
                .map(professorMapper::professorToProfessorDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProfessorDTO> findById(Long id) {
        return Optional.ofNullable(professorMapper.professorToProfessorDto(professorRepository.findById(id).orElse(null)));
    }

    @Override
    public List<CourseDTO> findCourses(Long id) {
        return null;
    }

    @Override
    public ProfessorDTO addProfessor(ProfessorDTO professor) {
        if (professorRepository.existsByNationalId(professor.getNationalId())) {
            throw new DuplicateKeyException("duplicate nationalId");
        }

        return professorMapper.professorToProfessorDto(professorRepository.save(professorMapper.professorDtoToProfessor(professor)));
    }

    @Override
    public Optional<ProfessorDTO> updateProfessorById(Long id, ProfessorDTO professor) {
        AtomicReference<Optional<ProfessorDTO>> atomicReference = new AtomicReference<>();

        professorRepository.findById(id).ifPresentOrElse(foundProfessor -> {
            if (professorRepository.existsByNationalId(professor.getNationalId())) {
                throw new DuplicateKeyException("duplicate nationalId");
            }

            foundProfessor.setFirstName(professor.getFirstName());
            foundProfessor.setLastName(professor.getLastName());
            foundProfessor.setNationalId(professor.getNationalId());
            foundProfessor.setUpdatedAt(LocalDateTime.now());

            atomicReference.set(Optional.of(professorMapper
                    .professorToProfessorDto(professorRepository.save(foundProfessor))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteProfessorById(Long id) {
        if (professorRepository.existsById(id)) {
            professorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
