package co.edu.unicauca.controllers;

import co.edu.unicauca.dtos.JwtResponseDTO;
import co.edu.unicauca.dtos.LoginRequestDTO;
import co.edu.unicauca.entities.Coordinator;
import co.edu.unicauca.entities.Director;
import co.edu.unicauca.entities.Student;
import co.edu.unicauca.exceptions.TokenRefreshException;
import co.edu.unicauca.services.AuthService;
import co.edu.unicauca.services.CoordinatorService;
import co.edu.unicauca.services.DirectorService;
import co.edu.unicauca.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    /**
     * POST /auth/login
     * Login - Retorna access token + refresh token
     */
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

    /**
     * POST /auth/refresh
     * Refresca el access token usando el refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody JwtResponseDTO request) {
        try {
            String refreshToken = request.getRefreshToken();

            if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseEntity.badRequest().body("Refresh token is required");
            }

            JwtResponseDTO response = authService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(response);

        } catch (TokenRefreshException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while refreshing token");
        }
    }

    /**
     * POST /auth/logout
     * Logout del dispositivo actual (revoca el refresh token)
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody JwtResponseDTO request) {
        try {
            String refreshToken = request.getRefreshToken();
            authService.logout(refreshToken);
            return ResponseEntity.ok("Logged out successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during logout");
        }
    }

    /**
     * POST /auth/logout-all
     * Logout de todos los dispositivos (requiere estar autenticado)
     */
    @PostMapping("/logout-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutAllDevices(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Extraer el ID de la cuenta del UserDetails
            Long accountId = ((co.edu.unicauca.authentication.AccountDetails) userDetails).getId();
            authService.logoutAllDevices(accountId);
            return ResponseEntity.ok("Logged out from all devices successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during logout");
        }
    }

    /**
     * POST /auth/register-student
     * Registro público de estudiantes
     */
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

    /**
     * POST /auth/register-coordinator
     * Registro de coordinadores (solo ADMIN)
     */
    @PostMapping("/register-coordinator")
    @PreAuthorize("hasRole('ADMIN')")
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

    /**
     * POST /auth/register-director
     * Registro de directores (solo ADMIN)
     */
    @PostMapping("/register-director")
    @PreAuthorize("hasRole('ADMIN')")
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