package ir.co.isc.task.controllers;

import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.models.EnrollmentDTO;
import ir.co.isc.task.models.StudentDTO;
import ir.co.isc.task.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class StudentController {

    public static final String STUDENTS_PATH = "/api/v1/students";
    public static final String STUDENTS_PATH_WITH_ID = STUDENTS_PATH + "/{studentId}";
    public static final String STUDENT_ID_PARAM_NAME = "studentId";
    public static final String STUDENT_COURSES_PATH = STUDENTS_PATH_WITH_ID + "/courses";
    public static final String ENROLL_COURSE_PATH = STUDENT_COURSES_PATH + "/enroll";
    public static final String DROP_COURSE_PATH = STUDENT_COURSES_PATH + "/drop";


    private final StudentService studentService;

    @GetMapping(STUDENTS_PATH)
    public Page<StudentDTO> getStudents(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        return studentService.findAll(pageNumber, pageSize);
    }

    @GetMapping(STUDENTS_PATH_WITH_ID)
    public StudentDTO getStudentById(@PathVariable(STUDENT_ID_PARAM_NAME) Long studentId) {
        return studentService.findById(studentId);
    }

    @GetMapping(STUDENT_COURSES_PATH)
    public Page<CourseDTO> getCoursesOfStudent(
            @PathVariable(STUDENT_ID_PARAM_NAME) Long studentId,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        return studentService.findCourses(studentId, pageNumber, pageSize);
    }

    @PostMapping(STUDENTS_PATH)
    public ResponseEntity addStudent(@Validated @RequestBody StudentDTO student) {
        StudentDTO savedStudent = studentService.addStudent(student);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", STUDENTS_PATH + "/" + savedStudent.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(STUDENTS_PATH_WITH_ID)
    public ResponseEntity updateStudent(@PathVariable(STUDENT_ID_PARAM_NAME) Long studentId, @Validated @RequestBody StudentDTO student) {
        studentService.updateStudentById(studentId, student);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(STUDENTS_PATH_WITH_ID)
    public ResponseEntity deleteStudent(@PathVariable(STUDENT_ID_PARAM_NAME) Long studentId) {
        studentService.deleteStudentById(studentId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(ENROLL_COURSE_PATH)
    public ResponseEntity enroll(@PathVariable(STUDENT_ID_PARAM_NAME) Long studentId,
                                 @Validated @RequestBody EnrollmentDTO enrollmentDto) {
        studentService.enrollCourse(studentId, enrollmentDto.getCourseId());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(DROP_COURSE_PATH)
    public ResponseEntity drop(@PathVariable(STUDENT_ID_PARAM_NAME) Long studentId,
                                 @Validated @RequestBody EnrollmentDTO enrollmentDto) {
        studentService.dropCourse(studentId, enrollmentDto.getCourseId());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
