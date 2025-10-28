package co.edu.unicauca.repositories;

import co.edu.unicauca.entities.Draft;
import co.edu.unicauca.entities.FormatA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DraftRepository extends JpaRepository<Draft, Long> {
}
