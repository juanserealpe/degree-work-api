package co.edu.unicauca.repositories;

import co.edu.unicauca.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
