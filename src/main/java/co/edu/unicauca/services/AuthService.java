package co.edu.unicauca.services;

import co.edu.unicauca.authentication.AccountDetails;
import co.edu.unicauca.dtos.JwtResponseDTO;
import co.edu.unicauca.dtos.LoginRequestDTO;
import co.edu.unicauca.entities.Account;
import co.edu.unicauca.entities.Student;
import co.edu.unicauca.enums.Role;
import co.edu.unicauca.repositories.AccountRepository;
import co.edu.unicauca.repositories.StudentRepository;
import co.edu.unicauca.utilities.JwtUtils;
import co.edu.unicauca.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Autentica un usuario y genera un JWT
     */
    public JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        Logger.info(getClass(), "Attempting login for email: " + loginRequest.getEmail());

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            AccountDetails userDetails = (AccountDetails) auth.getPrincipal();
            String token = jwtUtils.generateJwtToken(userDetails);

            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(a -> a.getAuthority())
                    .toList();

            Logger.success(getClass(), "Login successful for user ID: " + userDetails.getId()
                    + " | Roles: " + roles);

            return new JwtResponseDTO(token, userDetails.getId(), roles);

        } catch (BadCredentialsException e) {
            Logger.error(getClass(), "Login failed for email: " + loginRequest.getEmail()
                    + " | Reason: Invalid credentials");
            throw new BadCredentialsException("Invalid email or password");
        } catch (Exception e) {
            Logger.error(getClass(), "Login failed for email: " + loginRequest.getEmail()
                    + " | Reason: " + e.getMessage());
            throw new RuntimeException("Authentication error: " + e.getMessage());
        }
    }

    /**
     * Registra un nuevo estudiante
     */
    @Transactional
    public Student registerStudent(Student student) {
        Logger.info(getClass(), "Attempting to register new student with email: "
                + student.getAccount().getEmail());

        // Validar que el email no exista
        if (emailExists(student.getAccount().getEmail())) {
            Logger.warn(getClass(), "Registration failed — email already in use: "
                    + student.getAccount().getEmail());
            throw new IllegalArgumentException("Email already in use");
        }

        try {
            // Encriptar contraseña y asignar rol
            student.getAccount().setPassword(
                    passwordEncoder.encode(student.getAccount().getPassword())
            );
            student.getAccount().setRole(Role.STUDENT);

            // Guardar estudiante
            Student saved = studentRepository.save(student);

            Logger.success(getClass(), "Student registered successfully. ID: "
                    + saved.getIdPerson() + " | Email: " + saved.getAccount().getEmail());

            return saved;

        } catch (Exception e) {
            Logger.error(getClass(), "Unexpected error while registering student: "
                    + e.getMessage());
            throw new RuntimeException("Error registering student: " + e.getMessage());
        }
    }

    /**
     * Verifica si un email ya existe en el sistema
     */
    public boolean emailExists(String email) {
        return accountRepository.findByEmail(email).isPresent();
    }

    /**
     * Busca una cuenta por email
     */
    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
}