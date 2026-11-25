package com.gaselvolcan.catalogo.service;

public class PedidoNoEncontradoException extends RuntimeException {
    public PedidoNoEncontradoException(Long id) {
        super("Pedido no encontrado: " + id);
    }
}
