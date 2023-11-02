package ir.co.isc.task.services;

import ir.co.isc.task.domain.Student;

public interface StudentService {

    Iterable<Student> findAll();
}
