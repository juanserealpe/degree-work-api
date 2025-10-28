package co.edu.unicauca.repositories;

import co.edu.unicauca.entities.DegreeWork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DegreeWorkRepository extends JpaRepository<DegreeWork, Long> {
    Optional<DegreeWork> findByTittle(String tittle);
}
