package com.gaselvolcan.catalogo.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import com.gaselvolcan.catalogo.service.PedidoNoEncontradoException;
import com.gaselvolcan.catalogo.service.PedidoConflictoException;
import org.springframework.http.converter.HttpMessageNotReadableException;

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

    @ExceptionHandler(PedidoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> pedidoNoEncontrado(PedidoNoEncontradoException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", error.getMessage()));
    }

    @ExceptionHandler(PedidoConflictoException.class)
    public ResponseEntity<Map<String, String>> pedidoEnConflicto(PedidoConflictoException error) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", error.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, String>> solicitudInvalida(Exception error) {
        String mensaje = error instanceof IllegalArgumentException && error.getMessage() != null
                ? error.getMessage() : "La solicitud contiene datos inválidos";
        return ResponseEntity.badRequest().body(Map.of("error", mensaje));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> conflictoIntegridad() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Los datos entran en conflicto con un registro existente"));
    }
}
