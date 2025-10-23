package co.edu.unicauca.controllers;

import co.edu.unicauca.dtos.JwtResponseDTO;
import co.edu.unicauca.dtos.LoginRequestDTO;
import co.edu.unicauca.entities.Coordinator;
import co.edu.unicauca.entities.Director;
import co.edu.unicauca.entities.Student;
import co.edu.unicauca.services.AuthService;
import co.edu.unicauca.services.CoordinatorService;
import co.edu.unicauca.services.DirectorService;
import co.edu.unicauca.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CoordinatorService coordinatorService;

    @Autowired
    private DirectorService directorService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            JwtResponseDTO response = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during authentication");
        }
    }

    @PostMapping("/register-student")
    public ResponseEntity<?> registerStudent(@RequestBody Student student) {
        try {
            Student saved = studentService.registerStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering the student");
        }
    }

    @PostMapping("/register-coordinator")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerCoordinator(@RequestBody Coordinator coordinator) {
        try {
            Coordinator saved = coordinatorService.registerCoordinator(coordinator);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering the coordinator");
        }
    }

    @PostMapping("/register-director")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerDirector(@RequestBody Director director) {
        try {
            Director saved = directorService.registerDirector(director);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering the director");
        }
    }
}