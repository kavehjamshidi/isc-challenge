package ir.co.isc.task.services;

import ir.co.isc.task.models.StudentDTO;
import ir.co.isc.task.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Iterable<StudentDTO> findAll() {
        return null;
    }
}