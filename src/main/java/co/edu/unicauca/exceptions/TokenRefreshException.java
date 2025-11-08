package co.edu.unicauca.exceptions;

import co.edu.unicauca.enums.exceptions.RefreshTokenErrorCode;

public class TokenRefreshException extends RuntimeException {
    private final String token;
    private final RefreshTokenErrorCode errorCode;

    public TokenRefreshException(String token, RefreshTokenErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.token = token;
        this.errorCode = errorCode;
    }

    public String getToken() {
        return token;
    }

    public RefreshTokenErrorCode getErrorCode() {
        return errorCode;
    }
}
