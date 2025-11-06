package co.edu.unicauca.services.auth;

import co.edu.unicauca.dtos.user.UserCreateDTO;
import co.edu.unicauca.dtos.user.UserResponseDTO;
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

    /**
     * Registers a new user in the system.
     *
     * This method:
     * 1. Validates the user creation request.
     * 2. Ensures the email does not already exist.
     * 3. Normalizes user name fields.
     * 4. Creates and prepares an Account entity.
     * 5. Links the account to a new User entity.
     * 6. Saves the user and returns a response DTO.
     *
     * @param prmUser User creation request DTO
     * @return A UserResponseDTO with the created user information
     */
    @Transactional
    public UserResponseDTO userRegister(UserCreateDTO prmUser) {

        // Log the registration attempt
        Logger.info(getClass(), "Attempting to register new user: " + prmUser.getEmail());

        // Validate input data
        _userCreateValidator.validateRequest(prmUser);

        // Ensure the email does not already exist in the system
        _accountService.validateEmailNotExists(prmUser.getEmail());

        // Normalize user name fields before saving
        String normalizedNames = normalizeText(prmUser.getNames());
        String normalizedLastNames = normalizeText(prmUser.getLastNames());

        // Create a new Account entity with user credentials
        Account account = new Account();
        account.setEmail(prmUser.getEmail());
        account.setPassword(prmUser.getPassword());
        account.setRoles(prmUser.getRoles());

        // Prepare the account (e.g., encrypt password, assign default roles)
        _accountService.prepareAccountForRegistration(account);

        // Create a new User entity and link it to the account
        User user = new User();
        user.setNames(normalizedNames);
        user.setLastNames(normalizedLastNames);
        user.setAccount(account);

        // Save the user in the database
        User resultSave = _userRepository.save(user);

        // Log successful registration
        Logger.success(getClass(), "User registered successfully. ID: " + resultSave.getIdUser() + " - Roles: " + account.getRoles());

        // Build and return the response DTO
        return UserResponseDTO.builder()
                .idUser(user.getIdUser())
                .names(user.getNames())
                .lastNames(user.getLastNames())
                .email(user.getAccount().getEmail())
                .roles(user.getAccount().getRoles())
                .build();
    }

    /**
     * Normalizes a text by trimming spaces and converting it to title case.
     * Example: " jUaN  sEbAsTiAn " -> "Juan Sebastian"
     *
     * @param text The input string
     * @return The normalized string
     */
    private String normalizeText(String text) {
        if (text == null || text.isBlank()) return null;

        // Trim and convert to lowercase, then capitalize each word
        String[] words = text.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return sb.toString().trim();
    }
}
