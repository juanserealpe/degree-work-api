package co.edu.unicauca.dtos;

import java.util.List;

public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";
    private Long idUser;
    private List<String> roles;

    public JwtResponseDTO(String token, Long iduser, List<String> roles) {
        this.token = token;
        this.idUser = iduser;
        this.roles = roles;
    }

    public String getToken() { return token; }
    public String getType() { return type; }
    public Long getIdUser() { return idUser; }
    public List<String> getRoles() { return roles; }
}
