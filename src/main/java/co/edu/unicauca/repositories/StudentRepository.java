package co.edu.unicauca.repositories;

import co.edu.unicauca.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
