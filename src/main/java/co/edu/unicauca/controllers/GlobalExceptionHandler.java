package co.edu.unicauca.controllers;

import co.edu.unicauca.dtos.ErrorDTO;
import co.edu.unicauca.exceptions.TokenRefreshException;
import co.edu.unicauca.exceptions.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDTO> handleUserException(UserException ex) {
        ErrorDTO error = ErrorDTO.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .status(ex.getHttpStatus())
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }
    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<Object> handleTokenRefreshException(TokenRefreshException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", ex.getErrorCode().getCode());
        body.put("error", ex.getErrorCode().getDefaultMessage());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, ex.getErrorCode().getHttpStatus());
    }

    // puedes agregar más manejadores si quieres
}
