package ir.co.isc.task.controllers;

import ir.co.isc.task.controllers.exceptions.NotFoundException;
import ir.co.isc.task.models.ProfessorDTO;
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
    public List<ProfessorDTO> getProfessors() {
        return professorService.findAll();
    }

    @GetMapping( PROFESSORS_PATH_WITH_ID)
    public ProfessorDTO getProfessorById(@PathVariable(PROFESSOR_ID_PARAM_NAME) Long professorId) {
      return professorService.findById(professorId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(PROFESSORS_PATH)
    public ResponseEntity addProfessor(@RequestBody ProfessorDTO professor) {
        ProfessorDTO savedProfessor = professorService.addProfessor(professor);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location",PROFESSORS_PATH + "/" + savedProfessor.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping( PROFESSORS_PATH_WITH_ID)
    public ResponseEntity updateProfessor(@PathVariable(PROFESSOR_ID_PARAM_NAME) Long professorId, @RequestBody ProfessorDTO professor) {
        if(professorService.updateProfessorById(professorId, professor).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(PROFESSORS_PATH_WITH_ID)
    public ResponseEntity deleteProfessor(@PathVariable(PROFESSOR_ID_PARAM_NAME) Long professorId) {
        log.info(professorId.toString());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
