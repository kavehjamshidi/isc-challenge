package ir.co.isc.task.controllers;

import ir.co.isc.task.controllers.exceptions.NotFoundException;
import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.services.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CourseController {

    public static final String COURSES_PATH = "/api/v1/courses";
    public static final String COURSES_PATH_WITH_ID = COURSES_PATH + "/{courseId}";
    public static final String COURSE_ID_PARAM_NAME = "courseId";

    private final CourseService courseService;

    @GetMapping(COURSES_PATH)
    public List<CourseDTO> getCourses() {
        return courseService.findAll();
    }

    @GetMapping(COURSES_PATH_WITH_ID)
    public CourseDTO getCourseById(@PathVariable(COURSE_ID_PARAM_NAME) Long courseId) {
        return courseService.findById(courseId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(COURSES_PATH)
    public ResponseEntity addCourse(@Validated @RequestBody CourseDTO course) {
        CourseDTO savedCourse = courseService.addCourse(course);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", COURSES_PATH + "/" + savedCourse.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(COURSES_PATH_WITH_ID)
    public ResponseEntity updateCourse(@Validated @PathVariable(COURSE_ID_PARAM_NAME) Long courseId, @RequestBody CourseDTO course) {
        if (courseService.updateCourseById(courseId, course).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(COURSES_PATH_WITH_ID)
    public ResponseEntity deleteCourse(@PathVariable(COURSE_ID_PARAM_NAME) Long courseId) {
        if (!courseService.deleteCourseById(courseId)) {
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
