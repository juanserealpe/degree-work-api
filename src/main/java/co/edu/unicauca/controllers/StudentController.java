package co.edu.unicauca.controllers;


import co.edu.unicauca.entities.DegreeWork;
import co.edu.unicauca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private UserRepository studentRepository;
}
