package com.hotel.guest.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de validaciones de DTO (ej. @Email inválido, o @NotBlank vacío)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Manejo de reglas de negocio (ej. "El huésped ya existe" o "Huésped no encontrado")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeExceptions(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        
        // Si el mensaje es "Huésped no encontrado", devolvemos un 404, de lo contrario un 400
        if (ex.getMessage().contains("no encontrado")) {
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
}
