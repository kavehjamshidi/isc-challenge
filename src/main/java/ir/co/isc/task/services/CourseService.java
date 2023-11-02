package ir.co.isc.task.services;

import ir.co.isc.task.models.CourseDTO;

public interface CourseService {

    Iterable<CourseDTO> findAll();
}

