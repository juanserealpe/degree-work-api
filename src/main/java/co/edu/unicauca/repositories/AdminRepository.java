package co.edu.unicauca.repositories;

import co.edu.unicauca.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Long> {

}
