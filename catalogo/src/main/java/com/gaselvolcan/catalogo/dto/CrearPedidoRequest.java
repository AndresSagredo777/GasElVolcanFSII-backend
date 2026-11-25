package com.gaselvolcan.catalogo.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CrearPedidoRequest(
        @NotBlank @Size(max = 150) String nombreDestinatario,
        @NotBlank @Size(max = 30) String telefono,
        @NotBlank @Size(max = 255) String direccion,
        @NotEmpty @Size(max = 50) List<@Valid LineaPedidoRequest> productos) {
}
