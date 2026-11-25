package com.gaselvolcan.catalogo.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(ProductoNoEncontradoException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", error.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> datosInvalidos(MethodArgumentNotValidException error) {
        String mensaje = error.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(campo -> campo.getField() + ": " + campo.getDefaultMessage())
                .orElse("Datos inválidos");
        return ResponseEntity.badRequest().body(Map.of("error", mensaje));
    }
}
