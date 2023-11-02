package ir.co.isc.task.controllers;

import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.services.CourseService;
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
//@RequestMapping("/api/v1/courses")
public class CourseController {

    public static final String COURSES_PATH = "/api/v1/courses";
    public static final String COURSES_PATH_WITH_ID = COURSES_PATH + "/{courseId}";
    public static final String COURSE_ID_PARAM_NAME = "courseId";

    private final CourseService courseService;

    @GetMapping(COURSES_PATH)
    public List<CourseDTO> getCourses() {

        CourseDTO course1 = CourseDTO.builder()
                .id(123456L).build();

        List<CourseDTO> list = new ArrayList<>();

        list.add(course1);

        return list;
    }

    @GetMapping( COURSES_PATH_WITH_ID)
    public CourseDTO getCourseById(@PathVariable(COURSE_ID_PARAM_NAME) Long courseId) {

        CourseDTO course1 = CourseDTO.builder()
                .id(courseId).build();

        return course1;
    }

    @PostMapping(COURSES_PATH)
    public ResponseEntity addCourse(@RequestBody CourseDTO course) {
        course.setId(123465L);

        log.info(course.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location",COURSES_PATH + "/" + course.getId());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping( COURSES_PATH_WITH_ID)
    public ResponseEntity updateCourse(@PathVariable(COURSE_ID_PARAM_NAME) Long courseId, @RequestBody CourseDTO course) {
        log.info(courseId.toString());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping( COURSES_PATH_WITH_ID)
    public ResponseEntity partialUpdateCourse(@PathVariable(COURSE_ID_PARAM_NAME) Long courseId, @RequestBody CourseDTO course) {
        log.info(courseId.toString());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(COURSES_PATH_WITH_ID)
    public ResponseEntity deleteCourse(@PathVariable(COURSE_ID_PARAM_NAME) Long courseId) {
        log.info(courseId.toString());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
