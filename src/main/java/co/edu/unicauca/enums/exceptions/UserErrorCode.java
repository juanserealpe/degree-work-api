package co.edu.unicauca.enums.exceptions;

import org.springframework.http.HttpStatus;

public enum UserErrorCode {

    // user not found
    USER_NOT_FOUND("U-404", "User not found", HttpStatus.NOT_FOUND),

    // register / update
    INVALID_EMAIL("U-400", "Invalid email format", HttpStatus.BAD_REQUEST),
    EMAIL_DOMAIN_INVALID("U-400", "Email must end with @unicauca.edu.co", HttpStatus.BAD_REQUEST),
    DUPLICATE_EMAIL("U-409", "Email already exists", HttpStatus.CONFLICT),

    INVALID_NAMES("U-400", "Names cannot be empty or exceed maximum length", HttpStatus.BAD_REQUEST),
    INVALID_LAST_NAMES("U-400", "Last names cannot be empty or exceed maximum length", HttpStatus.BAD_REQUEST),

    INVALID_PASSWORD("U-400", "Password must have at least 1 uppercase letter, 1 number and be longer than 6 characters", HttpStatus.BAD_REQUEST),

    // login / authentication
    INVALID_CREDENTIALS("U-401", "Invalid email or password", HttpStatus.UNAUTHORIZED),
    ACCOUNT_LOCKED("U-403", "Account is locked", HttpStatus.FORBIDDEN),

    // general user operations
    USER_ALREADY_ACTIVE("U-409", "User is already active", HttpStatus.CONFLICT),
    USER_INACTIVE("U-403", "User is inactive", HttpStatus.FORBIDDEN);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;

    UserErrorCode(String code, String defaultMessage, HttpStatus httpStatus) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
