package co.edu.unicauca.exceptions;

import co.edu.unicauca.enums.exceptions.UserErrorCode;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final String code;
    private final String message;
    private final org.springframework.http.HttpStatus httpStatus;

    public UserException(UserErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getDefaultMessage();
        this.httpStatus = errorCode.getHttpStatus();
    }

    public UserException(UserErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.code = errorCode.getCode();
        this.message = customMessage;
        this.httpStatus = errorCode.getHttpStatus();
    }
}
