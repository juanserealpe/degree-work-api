package co.edu.unicauca.dtos.userDTOs;

import co.edu.unicauca.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponseDTO {
    private Long idUser;
    private String names;
    private String lastNames;
    private String email;
    private Set<Role> roles;
}
