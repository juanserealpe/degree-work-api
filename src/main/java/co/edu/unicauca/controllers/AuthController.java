package co.edu.unicauca.controllers;

import co.edu.unicauca.dtos.JwtResponseDTO;
import co.edu.unicauca.dtos.LoginRequestDTO;
import co.edu.unicauca.entities.Admin;
import co.edu.unicauca.entities.User;
import co.edu.unicauca.enums.Role;
import co.edu.unicauca.exceptions.TokenRefreshException;
import co.edu.unicauca.services.AdminService;
import co.edu.unicauca.services.AuthService;
import co.edu.unicauca.services.UserService;
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
    private AuthService _authService;

    @Autowired
    private AdminService _adminService;

    @Autowired
    private UserService _userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            JwtResponseDTO response = _authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during authentication");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody JwtResponseDTO request) {
        try {
            String refreshToken = request.getRefreshToken();

            if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseEntity.badRequest().body("Refresh token is required");
            }

            JwtResponseDTO response = _authService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(response);

        } catch (TokenRefreshException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while refreshing token");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody JwtResponseDTO request) {
        try {
            String refreshToken = request.getRefreshToken();
            _authService.logout(refreshToken);
            return ResponseEntity.ok("Logged out successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during logout");
        }
    }

    @PostMapping("/logout-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutAllDevices(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            Long accountId = ((co.edu.unicauca.authentication.AccountDetails) userDetails).getId();
            _authService.logoutAllDevices(accountId);
            return ResponseEntity.ok("Logged out from all devices successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during logout");
        }
    }

    @PostMapping("/register-student")
    public ResponseEntity<?> registerStudent(@RequestBody User user) {
        try {
            User saved = _userService.userRegister(user, Role.STUDENT);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering the student");
        }
    }

    @PostMapping("/register-coordinator")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerCoordinator(@RequestBody User user) {
        try {
            User saved = _userService.userRegister(user, Role.COORDINATOR);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering the coordinator");
        }
    }

    @PostMapping("/register-director")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerDirector(@RequestBody User user) {
        try {
            User saved = _userService.userRegister(user, Role.DIRECTOR);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering the director");
        }
    }

    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin user) {
        try {
            Admin saved = _adminService.adminRegister(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering the director");
        }
    }
}