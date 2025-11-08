package co.edu.unicauca.enums.exceptions;

import org.springframework.http.HttpStatus;

public enum RefreshTokenErrorCode {

    // token not found
    TOKEN_NOT_FOUND("R-404", "Refresh token not found", HttpStatus.NOT_FOUND),

    // token expired
    TOKEN_EXPIRED("R-401", "Refresh token has expired", HttpStatus.UNAUTHORIZED),

    // token revoked
    TOKEN_REVOKED("R-403", "Refresh token has been revoked", HttpStatus.FORBIDDEN),

    // invalid token format or corrupted
    TOKEN_INVALID("R-400", "Invalid or malformed refresh token", HttpStatus.BAD_REQUEST),

    // error while creating token
    TOKEN_CREATION_FAILED("R-500", "Error generating refresh token", HttpStatus.INTERNAL_SERVER_ERROR),

    // error while revoking token
    TOKEN_REVOCATION_FAILED("R-500", "Error revoking refresh token", HttpStatus.INTERNAL_SERVER_ERROR),

    // error while deleting expired tokens
    TOKEN_CLEANUP_FAILED("R-500", "Error cleaning up expired tokens", HttpStatus.INTERNAL_SERVER_ERROR),

    // database-related error while handling refresh tokens
    DATABASE_ERROR("R-501", "Database error while handling refresh token", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;

    RefreshTokenErrorCode(String code, String defaultMessage, HttpStatus httpStatus) {
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
