package com.gaselvolcan.catalogo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaselvolcan.catalogo.dto.ActualizarEstadoPedidoRequest;
import com.gaselvolcan.catalogo.dto.CrearPedidoRequest;
import com.gaselvolcan.catalogo.dto.LineaPedidoRequest;
import com.gaselvolcan.catalogo.dto.PedidoResponse;
import com.gaselvolcan.catalogo.exception.ProductoNoEncontradoException;
import com.gaselvolcan.catalogo.model.DetallePedido;
import com.gaselvolcan.catalogo.model.EstadoPedido;
import com.gaselvolcan.catalogo.model.Pedido;
import com.gaselvolcan.catalogo.model.Producto;
import com.gaselvolcan.catalogo.repository.PedidoRepository;
import com.gaselvolcan.catalogo.repository.ProductoRepository;

@Service
public class PedidoService {
    private final PedidoRepository pedidos;
    private final ProductoRepository productos;

    public PedidoService(PedidoRepository pedidos, ProductoRepository productos) {
        this.pedidos = pedidos;
        this.productos = productos;
    }

    @Transactional
    public PedidoResponse crear(String correoCliente, CrearPedidoRequest request) {
        Map<Long, Integer> cantidades = new TreeMap<>();
        for (LineaPedidoRequest linea : request.productos()) {
            int cantidad = Math.addExact(cantidades.getOrDefault(linea.productoId(), 0), linea.cantidad());
            if (cantidad > 100) {
                throw new IllegalArgumentException("La cantidad máxima por producto en un pedido es 100");
            }
            cantidades.put(linea.productoId(), cantidad);
        }

        Pedido pedido = new Pedido();
        pedido.setCorreoCliente(correoCliente);
        pedido.setNombreDestinatario(request.nombreDestinatario().trim());
        pedido.setTelefono(request.telefono().trim());
        pedido.setDireccion(request.direccion().trim());
        pedido.setEstado(EstadoPedido.RECIBIDO);

        long total = 0;
        List<DetallePedido> detalles = new ArrayList<>();
        for (Map.Entry<Long, Integer> entrada : cantidades.entrySet()) {
            Producto producto = productos.buscarPorIdParaActualizar(entrada.getKey())
                    .orElseThrow(() -> new ProductoNoEncontradoException(entrada.getKey()));
            int cantidad = entrada.getValue();
            if (producto.getStock() < cantidad) {
                throw new PedidoConflictoException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            long subtotal = Math.multiplyExact((long) producto.getPrecio(), cantidad);
            total = Math.addExact(total, subtotal);
            producto.setStock(producto.getStock() - cantidad);

            DetallePedido detalle = new DetallePedido();
            detalle.setProductoId(producto.getId());
            detalle.setCodigo(producto.getCodigo());
            detalle.setNombreProducto(producto.getNombre());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setCantidad(cantidad);
            detalle.setSubtotal(subtotal);
            detalles.add(detalle);
        }
        pedido.setTotal(total);
        detalles.forEach(pedido::agregarDetalle);
        return convertir(pedidos.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarTodos() {
        return pedidos.findAllByOrderByCreadoEnDesc().stream().map(this::convertir).toList();
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarDelCliente(String correoCliente) {
        return pedidos.findByCorreoClienteOrderByCreadoEnDesc(correoCliente).stream().map(this::convertir).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscar(Long id, String correoCliente, boolean administrador) {
        Pedido pedido = buscarEntidad(id);
        if (!administrador && !Objects.equals(pedido.getCorreoCliente(), correoCliente)) {
            throw new PedidoNoEncontradoException(id);
        }
        return convertir(pedido);
    }

    @Transactional
    public PedidoResponse actualizarEstado(Long id, ActualizarEstadoPedidoRequest request) {
        Pedido pedido = buscarEntidad(id);
        EstadoPedido actual = pedido.getEstado();
        EstadoPedido nuevo = request.estado();
        if (!transicionPermitida(actual, nuevo)) {
            throw new PedidoConflictoException("No se permite cambiar el estado " + actual + " a " + nuevo);
        }

        if (nuevo == EstadoPedido.CANCELADO) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                productos.buscarPorIdParaActualizar(detalle.getProductoId()).ifPresent(producto ->
                        producto.setStock(Math.addExact(producto.getStock(), detalle.getCantidad())));
            }
        }
        pedido.setEstado(nuevo);
        return convertir(pedido);
    }

    private Pedido buscarEntidad(Long id) {
        return pedidos.findById(id).orElseThrow(() -> new PedidoNoEncontradoException(id));
    }

    private boolean transicionPermitida(EstadoPedido actual, EstadoPedido nuevo) {
        if (actual == nuevo) {
            return false;
        }
        return switch (actual) {
            case RECIBIDO -> nuevo == EstadoPedido.PREPARANDO || nuevo == EstadoPedido.CANCELADO;
            case PREPARANDO -> nuevo == EstadoPedido.DESPACHADO || nuevo == EstadoPedido.CANCELADO;
            case DESPACHADO -> nuevo == EstadoPedido.ENTREGADO;
            case ENTREGADO, CANCELADO -> false;
        };
    }

    private PedidoResponse convertir(Pedido pedido) {
        List<PedidoResponse.DetallePedidoResponse> lineas = pedido.getDetalles().stream()
                .map(detalle -> new PedidoResponse.DetallePedidoResponse(
                        detalle.getProductoId(), detalle.getCodigo(), detalle.getNombreProducto(),
                        detalle.getPrecioUnitario(), detalle.getCantidad(), detalle.getSubtotal()))
                .toList();
        return new PedidoResponse(pedido.getId(), pedido.getCorreoCliente(), pedido.getNombreDestinatario(),
                pedido.getTelefono(), pedido.getDireccion(), pedido.getEstado(), pedido.getTotal(),
                pedido.getCreadoEn(), lineas);
    }
}
