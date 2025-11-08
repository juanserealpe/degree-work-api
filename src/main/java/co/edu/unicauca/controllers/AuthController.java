package co.edu.unicauca.controllers;

import co.edu.unicauca.dtos.login.LoginResponseDTO;
import co.edu.unicauca.dtos.login.LoginRequestDTO;
import co.edu.unicauca.dtos.login.RefreshRequestDTO;
import co.edu.unicauca.dtos.user.UserCreateDTO;
import co.edu.unicauca.dtos.user.UserResponseDTO;
import co.edu.unicauca.services.auth.AuthService;
import co.edu.unicauca.services.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService _authService;

    @Autowired
    private UserService _userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = _authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequestDTO request) {
        LoginResponseDTO response = _authService.refreshAccessToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequestDTO request) {
        _authService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/logout-all")
    //@PreAuthorize("isAuthenticated()")
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

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserCreateDTO user) {
        UserResponseDTO saved = _userService.userRegister(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}