package co.edu.unicauca.services;

import co.edu.unicauca.entities.Coordinator;
import co.edu.unicauca.enums.Role;
import co.edu.unicauca.repositories.CoordinatorRepository;
import co.edu.unicauca.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoordinatorService {

    @Autowired
    private CoordinatorRepository coordinatorRepository;

    @Autowired
    private AccountService accountService;

    @Transactional
    public Coordinator registerCoordinator(Coordinator coordinator) {
        Logger.info(getClass(), "Attempting to register new coordinator: "
                + coordinator.getAccount().getEmail());
        accountService.validateEmailNotExists(coordinator.getAccount().getEmail());
        accountService.prepareAccountForRegistration(coordinator.getAccount());
        coordinator.getAccount().setRole(Role.COORDINATOR);
        validateCoordinatorData(coordinator);
        Coordinator saved = coordinatorRepository.save(coordinator);

        Logger.success(getClass(), "Coordinator registered successfully. ID: "
                + saved.getIdPerson());

        return saved;
    }


    private void validateCoordinatorData(Coordinator coordinator) {
        if (coordinator.getNames() == null || coordinator.getNames().isBlank())
                throw new IllegalArgumentException("Coordinator names are required");

        if (coordinator.getLastNames() == null || coordinator.getLastNames().isBlank())
                throw new IllegalArgumentException("Coordinator last names are required");

    }
}