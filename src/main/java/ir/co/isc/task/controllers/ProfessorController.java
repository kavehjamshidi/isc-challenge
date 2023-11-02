package ir.co.isc.task.controllers;

import ir.co.isc.task.models.Professor;
import ir.co.isc.task.services.ProfessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProfessorController {
    public static final String PROFESSORS_PATH = "/api/v1/professors";
    public static final String PROFESSORS_PATH_WITH_ID = PROFESSORS_PATH + "/{professorId}";
    public static final String PROFESSOR_ID_PARAM_NAME = "professorId";


    private final ProfessorService professorService;

    @GetMapping(PROFESSORS_PATH)
    public List<Professor> getProfessors() {

        List<Professor> list = new ArrayList<>();

        return list;
    }

    @GetMapping( PROFESSORS_PATH_WITH_ID)
    public Professor getProfessorById(@PathVariable(PROFESSOR_ID_PARAM_NAME) Long professorId) {

        Professor professor1 = Professor.builder()
                .id(professorId).build();

        return professor1;
    }

    @PostMapping(PROFESSORS_PATH)
    public ResponseEntity addProfessor(@RequestBody Professor professor) {
        professor.setId(123465L);

        log.info(professor.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location",PROFESSORS_PATH + "/" + professor.getId());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping( PROFESSORS_PATH_WITH_ID)
    public ResponseEntity updateProfessor(@PathVariable(PROFESSOR_ID_PARAM_NAME) Long professorId, @RequestBody Professor professor) {
        log.info(professorId.toString());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping( PROFESSORS_PATH_WITH_ID)
    public ResponseEntity partialUpdateProfessor(@PathVariable(PROFESSOR_ID_PARAM_NAME) Long professorId, @RequestBody Professor professor) {
        log.info(professorId.toString());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(PROFESSORS_PATH_WITH_ID)
    public ResponseEntity deleteProfessor(@PathVariable(PROFESSOR_ID_PARAM_NAME) Long professorId) {
        log.info(professorId.toString());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
