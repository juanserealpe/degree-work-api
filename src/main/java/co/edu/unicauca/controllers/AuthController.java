package co.edu.unicauca.controllers;

import co.edu.unicauca.dtos.JwtResponseDTO;
import co.edu.unicauca.dtos.LoginRequestDTO;
import co.edu.unicauca.entities.Student;
import co.edu.unicauca.enums.Role;
import co.edu.unicauca.repositories.AccountRepository;
import co.edu.unicauca.repositories.StudentRepository;
import co.edu.unicauca.utilities.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .toList();

        return ResponseEntity.ok(
                new JwtResponseDTO(
                        token,
                        userDetails.getUsername(),
                        roles
                )
        );
    }

    @PostMapping("/register-student")
    public ResponseEntity<?> registerStudent(@RequestBody Student student) {
        if (accountRepository.findByEmail(student.getAccount().getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        student.getAccount().setPassword(passwordEncoder.encode(student.getAccount().getPassword()));
        student.getAccount().setRole(Role.STUDENT);
        Student saved = studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
