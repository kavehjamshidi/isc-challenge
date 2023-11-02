package ir.co.isc.task.services;

import ir.co.isc.task.domain.Course;

public interface CourseService {

    Iterable<Course> findAll();
}

