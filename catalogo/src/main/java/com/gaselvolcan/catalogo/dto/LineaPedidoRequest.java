package com.gaselvolcan.catalogo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LineaPedidoRequest(
        @NotNull @Positive Long productoId,
        @NotNull @Positive @Max(100) Integer cantidad) {
}
