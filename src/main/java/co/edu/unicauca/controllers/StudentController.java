package co.edu.unicauca.controllers;


import co.edu.unicauca.entities.Student;
import co.edu.unicauca.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;


    @GetMapping("/list")
    @PreAuthorize("hasRole('STUDENT')")
    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }
}
