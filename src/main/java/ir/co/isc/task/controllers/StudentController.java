package ir.co.isc.task.controllers;

import ir.co.isc.task.models.StudentDTO;
import ir.co.isc.task.services.StudentService;
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
public class StudentController {
    public static final String STUDENTS_PATH = "/api/v1/students";
    public static final String STUDENTS_PATH_WITH_ID = STUDENTS_PATH + "/{studentId}";
    public static final String STUDENT_ID_PARAM_NAME = "studentId";


    private final StudentService studentService;

    @GetMapping(STUDENTS_PATH)
    public List<StudentDTO> getStudents() {
        
        List<StudentDTO> list = new ArrayList<>();
        
        return list;
    }

    @GetMapping( STUDENTS_PATH_WITH_ID)
    public StudentDTO getStudentById(@PathVariable(STUDENT_ID_PARAM_NAME) Long studentId) {

        StudentDTO student1 = StudentDTO.builder()
                .id(studentId).build();

        return student1;
    }

    @PostMapping(STUDENTS_PATH)
    public ResponseEntity addStudent(@RequestBody StudentDTO student) {
        student.setId(123465L);

        log.info(student.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location",STUDENTS_PATH + "/" + student.getId());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping( STUDENTS_PATH_WITH_ID)
    public ResponseEntity updateStudent(@PathVariable(STUDENT_ID_PARAM_NAME) Long studentId, @RequestBody StudentDTO student) {
        log.info(studentId.toString());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping( STUDENTS_PATH_WITH_ID)
    public ResponseEntity partialUpdateStudent(@PathVariable(STUDENT_ID_PARAM_NAME) Long studentId, @RequestBody StudentDTO student) {
        log.info(studentId.toString());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(STUDENTS_PATH_WITH_ID)
    public ResponseEntity deleteStudent(@PathVariable(STUDENT_ID_PARAM_NAME) Long studentId) {
        log.info(studentId.toString());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
}
