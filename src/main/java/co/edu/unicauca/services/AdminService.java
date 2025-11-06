package co.edu.unicauca.services;

import co.edu.unicauca.entities.Admin;
import co.edu.unicauca.enums.Role;
import co.edu.unicauca.repositories.AdminRepository;
import co.edu.unicauca.services.auth.AccountService;
import co.edu.unicauca.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {
    @Autowired
    private AdminRepository _adminRepository;
    @Autowired
    private AccountService _accountService;

    @Transactional
    public Admin adminRegister(Admin prmAdmin){
        Logger.info(getClass(), "Attempting to register new admin:" +  prmAdmin.getAccount().getEmail());
        prmAdmin.getAccount().addRole(Role.ADMIN);
        _accountService.validateEmailNotExists(prmAdmin.getAccount().getEmail());
        _accountService.prepareAccountForRegistration(prmAdmin.getAccount());

        return _adminRepository.save(prmAdmin);
    }
}
