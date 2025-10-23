package co.edu.unicauca.repositories;

import co.edu.unicauca.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    // Buscar todos los estudiantes
    @Query("SELECT u FROM User u JOIN u.account.roles r WHERE r.name = 'STUDENT'")
    List<User> findAllStudents();

    // Buscar todos los coordinadores
    @Query("SELECT u FROM User u JOIN u.account.roles r WHERE r.name = 'COORDINATOR'")
    List<User> findAllCoordinators();

    // Buscar por rol y estado
    @Query("SELECT u FROM User u JOIN u.account.roles r WHERE r.name = :role AND u.active = true")
    List<User> findByRoleAndActive(@Param("role") String role);
}
