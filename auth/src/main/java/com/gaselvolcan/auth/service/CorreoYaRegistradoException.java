package com.gaselvolcan.auth.service;

public class CorreoYaRegistradoException extends RuntimeException {
    public CorreoYaRegistradoException() {
        super("Ya existe una cuenta con ese correo");
    }
}
