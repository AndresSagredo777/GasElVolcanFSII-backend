package com.gaselvolcan.catalogo.service;

public class PedidoConflictoException extends RuntimeException {
    public PedidoConflictoException(String mensaje) {
        super(mensaje);
    }
}
