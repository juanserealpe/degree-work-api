package co.edu.unicauca.controllers;


import co.edu.unicauca.entities.Student;
import co.edu.unicauca.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    @PostMapping
    public Student createStudent(@RequestBody Student prmStudent){
        return studentRepository.save(prmStudent);
    }
}
