package com.gaselvolcan.catalogo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.gaselvolcan.catalogo.model.EstadoPedido;

public record PedidoResponse(
        Long id,
        String correoCliente,
        String nombreDestinatario,
        String telefono,
        String direccion,
        EstadoPedido estado,
        Long total,
        LocalDateTime creadoEn,
        List<DetallePedidoResponse> productos) {

    public record DetallePedidoResponse(
            Long productoId,
            String codigo,
            String nombre,
            Integer precioUnitario,
            Integer cantidad,
            Long subtotal) {
    }
}
