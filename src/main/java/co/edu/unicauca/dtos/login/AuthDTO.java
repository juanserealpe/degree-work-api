package co.edu.unicauca.dtos.login;

public record AuthDTO(
        String accessToken,
        String refreshToken,
        String tokenType
) {}
