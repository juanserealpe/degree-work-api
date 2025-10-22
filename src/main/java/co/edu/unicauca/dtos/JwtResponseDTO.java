package co.edu.unicauca.dtos;

import java.util.List;

public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";
    private String email;
    private List<String> roles;

    public JwtResponseDTO(String token, String email, List<String> roles) {
        this.token = token;
        this.email = email;
        this.roles = roles;
    }

    public JwtResponseDTO(String newJwt) {
    }

    public String getToken() { return token; }
    public String getType() { return type; }
    public String getEmail() { return email; }
    public List<String> getRoles() { return roles; }
}
