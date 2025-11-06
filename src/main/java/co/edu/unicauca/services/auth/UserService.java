package co.edu.unicauca.services.auth;

import co.edu.unicauca.dtos.userDTOs.UserCreateDTO;
import co.edu.unicauca.dtos.userDTOs.UserResponseDTO;
import co.edu.unicauca.entities.Account;
import co.edu.unicauca.entities.User;
import co.edu.unicauca.repositories.UserRepository;
import co.edu.unicauca.utilities.Logger;
import co.edu.unicauca.validators.UserCreateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private AccountService _accountService;
    @Autowired
    private UserCreateValidator _userCreateValidator;

    @Transactional
    public UserResponseDTO userRegister(UserCreateDTO prmUser) {

        Logger.info(getClass(), "Attempting to register new user:" + prmUser.getEmail());

        _userCreateValidator.validateRequest(prmUser);
        _accountService.validateEmailNotExists(prmUser.getEmail());


        Account account = new Account();
        account.setEmail(prmUser.getEmail());
        account.setPassword(prmUser.getPassword());
        account.setRoles(prmUser.getRoles());
        _accountService.prepareAccountForRegistration(account);

        User user = new User();
        user.setNames(prmUser.getNames());
        user.setLastNames(prmUser.getLastNames());
        user.setAccount(account);

        User resultSave = _userRepository.save(user);
        Logger.success(getClass(), "User registered successfully. ID: " + resultSave.getIdUser() + " - Roles: " + account.getRoles());

        return UserResponseDTO.builder()
                .idUser(user.getIdUser())
                .names(user.getNames())
                .lastNames(user.getLastNames())
                .email(user.getAccount().getEmail())
                .roles(user.getAccount().getRoles())
                .build();
    }
}
