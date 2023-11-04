package ir.co.isc.task.controllers;

import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.models.ProfessorDTO;
import ir.co.isc.task.services.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ProfessorController {

    public static final String PROFESSORS_PATH = "/api/v1/professors";
    public static final String PROFESSORS_PATH_WITH_ID = PROFESSORS_PATH + "/{professorId}";
    public static final String PROFESSOR_ID_PARAM_NAME = "professorId";
    public static final String PROFESSOR_COURSES_PATH = PROFESSORS_PATH_WITH_ID + "/courses";

    private final ProfessorService professorService;

    @GetMapping(PROFESSORS_PATH)
    public Page<ProfessorDTO> getProfessors(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        return professorService.findAll(pageNumber, pageSize);
    }

    @GetMapping(PROFESSORS_PATH_WITH_ID)
    public ProfessorDTO getProfessorById(@PathVariable(PROFESSOR_ID_PARAM_NAME) Long professorId) {
        return professorService.findById(professorId);
    }

    @GetMapping(PROFESSOR_COURSES_PATH)
    public Page<CourseDTO> getCoursesOfProfessor(
            @PathVariable(PROFESSOR_ID_PARAM_NAME) Long professorId,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        return professorService.findCourses(professorId, pageNumber, pageSize);
    }

    @PostMapping(PROFESSORS_PATH)
    public ResponseEntity addProfessor(@Validated @RequestBody ProfessorDTO professor) {
        ProfessorDTO savedProfessor = professorService.addProfessor(professor);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", PROFESSORS_PATH + "/" + savedProfessor.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(PROFESSORS_PATH_WITH_ID)
    public ResponseEntity updateProfessor(@PathVariable(PROFESSOR_ID_PARAM_NAME) Long professorId, @Validated @RequestBody ProfessorDTO professor) {
        professorService.updateProfessorById(professorId, professor);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(PROFESSORS_PATH_WITH_ID)
    public ResponseEntity deleteProfessor(@PathVariable(PROFESSOR_ID_PARAM_NAME) Long professorId) {
        professorService.deleteProfessorById(professorId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
