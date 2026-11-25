package com.gaselvolcan.catalogo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductoRequest(
        @NotBlank @Size(max = 150) String nombre,
        @NotBlank @Size(max = 100) String categoria,
        @NotNull @PositiveOrZero Integer precio,
        @PositiveOrZero Integer precioAnterior,
        @Min(0) @Max(100) Integer descuento,
        @NotNull @PositiveOrZero Integer stock,
        @NotNull Boolean oferta,
        @NotBlank @Size(max = 255) String imagen,
        @Size(max = 1000) String descripcion) {
}
