package com.dcspa.prism.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) cause;
            String fieldPath = ife.getPath().stream()
                    .map(Reference::getFieldName)
                    .filter(name -> name != null && !name.isBlank())
                    .collect(Collectors.joining("."));
            if (fieldPath.isBlank()) {
                fieldPath = "(champ inconnu)";
            }

            String invalidValue = String.valueOf(ife.getValue());
            Class<?> targetType = ife.getTargetType();

            if (LocalDate.class.equals(targetType)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Format invalide pour '" + fieldPath + "'. Valeur reçue: '" + invalidValue
                                + "'. Formats attendus: yyyy-MM-dd (ex: 2026-03-17) "
                                + "ou dd/MM/yyyy (ex: 17/03/2026).");
            }

            if (LocalDateTime.class.equals(targetType)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Format invalide pour '" + fieldPath + "'. Valeur reçue: '" + invalidValue
                                + "'. Format attendu: yyyy-MM-dd'T'HH:mm:ss "
                                + "(ex: 2026-03-17T14:30:00).");
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Format invalide pour '" + fieldPath + "'. Valeur reçue: '" + invalidValue + "'.");
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Requête invalide.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage() != null ? ex.getMessage() : "Paramètres invalides.");
    }
}

