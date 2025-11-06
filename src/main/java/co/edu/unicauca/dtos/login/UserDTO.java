package co.edu.unicauca.dtos.login;

import java.util.List;

public record UserDTO(
        String email,
        List<String> roles
) {}