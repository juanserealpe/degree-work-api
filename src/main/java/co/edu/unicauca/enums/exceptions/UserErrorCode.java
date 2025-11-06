package co.edu.unicauca.enums.exceptions;

import org.springframework.http.HttpStatus;

public enum UserErrorCode {
    USER_NOT_FOUND("U-404", "User not found", HttpStatus.NOT_FOUND),
    INVALID_EMAIL("U-400", "Invalid email format", HttpStatus.BAD_REQUEST),
    DUPLICATE_EMAIL("U-409", "Email already exists", HttpStatus.CONFLICT);

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
