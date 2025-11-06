package co.edu.unicauca.dtos.login;

public record LoginResponseDTO(
        AuthDTO auth,
        UserDTO user
) {}