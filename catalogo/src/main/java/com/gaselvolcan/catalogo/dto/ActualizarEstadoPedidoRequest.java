package com.gaselvolcan.catalogo.dto;

import com.gaselvolcan.catalogo.model.EstadoPedido;
import jakarta.validation.constraints.NotNull;

public record ActualizarEstadoPedidoRequest(@NotNull EstadoPedido estado) {
}
