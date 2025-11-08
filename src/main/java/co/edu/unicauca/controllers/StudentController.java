package co.edu.unicauca.controllers;

import co.edu.unicauca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private UserRepository studentRepository;
}
