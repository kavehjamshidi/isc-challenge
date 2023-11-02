package ir.co.isc.task.services;

import ir.co.isc.task.models.StudentDTO;

public interface StudentService {

    Iterable<StudentDTO> findAll();
}
