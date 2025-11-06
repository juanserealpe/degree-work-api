package co.edu.unicauca.validators;

import co.edu.unicauca.dtos.userDTOs.UserCreateDTO;
import co.edu.unicauca.enums.exceptions.UserErrorCode;
import co.edu.unicauca.exceptions.UserException;
import co.edu.unicauca.utilities.Logger;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserCreateValidator {

    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_LASTNAME_LENGTH = 50;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final String EMAIL_DOMAIN = "@unicauca.edu.co";
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*\\d).{" + MIN_PASSWORD_LENGTH + ",}$");
    // at least 1 uppercase, 1 number, minimum 6 characters

    /**
     * Validates the UserCreateDTO object.
     * Throws UserException if any field is invalid.
     *
     * @param userCreateDTO the DTO object to validate
     */
    public void validateRequest(UserCreateDTO userCreateDTO) {
        Logger.info(getClass(), "Starting User request validation.");

        try {
            // validate names
            if (userCreateDTO.getNames() == null || userCreateDTO.getNames().isBlank()) {
                Logger.error(getClass(), "Names field is empty.");
                throw new UserException(UserErrorCode.INVALID_NAMES, "Names cannot be empty");
            }
            if (userCreateDTO.getNames().length() > MAX_NAME_LENGTH) {
                Logger.error(getClass(), "Names field exceeds max length.");
                throw new UserException(UserErrorCode.INVALID_NAMES,
                        "Names cannot exceed " + MAX_NAME_LENGTH + " characters");
            }

            // validate last names
            if (userCreateDTO.getLastNames() == null || userCreateDTO.getLastNames().isBlank()) {
                Logger.error(getClass(), "Last names field is empty.");
                throw new UserException(UserErrorCode.INVALID_LAST_NAMES, "Last names cannot be empty");
            }
            if (userCreateDTO.getLastNames().length() > MAX_LASTNAME_LENGTH) {
                Logger.error(getClass(), "Last names field exceeds max length.");
                throw new UserException(UserErrorCode.INVALID_LAST_NAMES,
                        "Last names cannot exceed " + MAX_LASTNAME_LENGTH + " characters");
            }

            // validate email
            if (userCreateDTO.getEmail() == null || userCreateDTO.getEmail().isBlank()) {
                Logger.error(getClass(), "Email field is empty.");
                throw new UserException(UserErrorCode.INVALID_EMAIL, "Email cannot be empty");
            }
            if (!userCreateDTO.getEmail().endsWith(EMAIL_DOMAIN)) {
                Logger.error(getClass(), "Email domain invalid: " + userCreateDTO.getEmail());
                throw new UserException(UserErrorCode.INVALID_EMAIL,
                        "Email must end with " + EMAIL_DOMAIN);
            }

            // validate password
            if (userCreateDTO.getPassword() == null || userCreateDTO.getPassword().isBlank()) {
                Logger.error(getClass(), "Password field is empty.");
                throw new UserException(UserErrorCode.INVALID_PASSWORD, "Password cannot be empty");
            }
            if (!PASSWORD_PATTERN.matcher(userCreateDTO.getPassword()).matches()) {
                Logger.error(getClass(), "Password does not meet complexity requirements.");
                throw new UserException(UserErrorCode.INVALID_PASSWORD,
                        "Password must have at least 1 uppercase letter, 1 number and be longer than "
                                + MIN_PASSWORD_LENGTH + " characters");
            }

            Logger.info(getClass(), "User request validation completed successfully.");
        } catch (UserException ex) {
            // log validation-specific exceptions
            Logger.warn(getClass(), "Validation failed: " + ex.getMessage());
            throw ex; // Rethrow to be handled by GlobalExceptionHandler
        } catch (Exception ex) {
            // log unexpected errors
            Logger.error(getClass(), "Unexpected error during validation: " + ex.getMessage());
            throw new UserException(UserErrorCode.INVALID_NAMES, "Unexpected validation error");
        }
    }
}
