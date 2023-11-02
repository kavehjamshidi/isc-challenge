package ir.co.isc.task.services;

import ir.co.isc.task.models.CourseDTO;
import ir.co.isc.task.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Iterable<CourseDTO> findAll() {
        return null;
    }
}
