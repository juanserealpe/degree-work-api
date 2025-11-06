package co.edu.unicauca.services;

import co.edu.unicauca.entities.Account;
import co.edu.unicauca.entities.User;
import co.edu.unicauca.enums.exceptions.UserErrorCode;
import co.edu.unicauca.enums.Role;
import co.edu.unicauca.exceptions.UserException;
import co.edu.unicauca.repositories.UserRepository;
import co.edu.unicauca.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private AccountService _accountService;

    @Transactional
    public User userRegister(User prmUser, Role... roles){
        if(prmUser != null){
            throw new UserException(UserErrorCode.USER_NOT_FOUND, "User not found");
        }
        Logger.info(getClass(), "Attempting to register new user:" +  prmUser.getAccount().getEmail());

        _accountService.validateEmailNotExists(prmUser.getAccount().getEmail());
        _accountService.prepareAccountForRegistration(prmUser.getAccount());

        Account account = prmUser.getAccount();
        for(Role role : roles){
            account.addRole(role);
        }

        User resultSave = _userRepository.save(prmUser);
        Logger.success(getClass(), "User registered successfully. ID: " + resultSave.getIdUser() + " - Roles: " + account.getRoles());

        return resultSave;
    }

}
