package co.edu.unicauca.controllers;

import co.edu.unicauca.dtos.ErrorDTO;
import co.edu.unicauca.exceptions.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    // puedes agregar más manejadores si quieres
}
