package co.edu.unicauca.controllers;


import co.edu.unicauca.entities.User;
import co.edu.unicauca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private UserRepository studentRepository;


    @GetMapping("/list")
    @PreAuthorize("hasRole('STUDENT')")
    public List<User> getAllStudents(){
        return studentRepository.findAll();
    }
}
