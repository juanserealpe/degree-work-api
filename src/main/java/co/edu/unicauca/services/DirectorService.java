package co.edu.unicauca.services;

import co.edu.unicauca.entities.Director;
import co.edu.unicauca.enums.Role;
import co.edu.unicauca.repositories.DirectorRepository;
import co.edu.unicauca.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DirectorService {

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private AccountService accountService;

    @Transactional
    public Director registerDirector(Director director) {
        Logger.info(getClass(), "Attempting to register new director: "
                + director.getAccount().getEmail());

        accountService.validateEmailNotExists(director.getAccount().getEmail());
        accountService.prepareAccountForRegistration(director.getAccount());
        director.getAccount().setRole(Role.DIRECTOR);

        validateDirectorData(director);

        Director saved = directorRepository.save(director);
        Logger.success(getClass(), "Director registered successfully. ID: " + saved.getIdPerson());

        return saved;
    }

    private void validateDirectorData(Director director) {
        if (director.getNames() == null || director.getNames().isBlank())
                throw new IllegalArgumentException("Director names are required");

        if (director.getLastNames() == null || director.getLastNames().isBlank())
                throw new IllegalArgumentException("Director last names are required");

    }
}