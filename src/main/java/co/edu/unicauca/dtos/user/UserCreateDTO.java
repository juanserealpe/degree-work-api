package co.edu.unicauca.dtos.user;

import co.edu.unicauca.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserCreateDTO {
    private String names;
    private String lastNames;
    private String email;
    private String password;
    private Set<Role> roles;
}
