package com.gaselvolcan.catalogo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gaselvolcan.catalogo.dto.ActualizarEstadoPedidoRequest;
import com.gaselvolcan.catalogo.dto.CrearPedidoRequest;
import com.gaselvolcan.catalogo.dto.LineaPedidoRequest;
import com.gaselvolcan.catalogo.model.DetallePedido;
import com.gaselvolcan.catalogo.model.EstadoPedido;
import com.gaselvolcan.catalogo.model.Pedido;
import com.gaselvolcan.catalogo.model.Producto;
import com.gaselvolcan.catalogo.repository.PedidoRepository;
import com.gaselvolcan.catalogo.repository.ProductoRepository;

class PedidoServiceTest {
    private PedidoRepository pedidos;
    private ProductoRepository productos;
    private PedidoService service;

    @BeforeEach
    void preparar() {
        pedidos = mock(PedidoRepository.class);
        productos = mock(ProductoRepository.class);
        service = new PedidoService(pedidos, productos);
        when(pedidos.save(any(Pedido.class))).thenAnswer(invocacion -> invocacion.getArgument(0));
    }

    @Test
    void creaPedidoConPreciosDelServidorYReservaStock() {
        Producto producto = producto(1L, 11_000, 8);
        when(productos.buscarPorIdParaActualizar(1L)).thenReturn(Optional.of(producto));

        var respuesta = service.crear("cliente@example.com", request(1L, 2));

        assertEquals(22_000L, respuesta.total());
        assertEquals(EstadoPedido.RECIBIDO, respuesta.estado());
        assertEquals(6, producto.getStock());
        assertEquals(11_000, respuesta.productos().getFirst().precioUnitario());
        verify(pedidos).save(any(Pedido.class));
    }

    @Test
    void rechazaPedidoCuandoNoHayStockSuficiente() {
        Producto producto = producto(1L, 11_000, 1);
        when(productos.buscarPorIdParaActualizar(1L)).thenReturn(Optional.of(producto));

        assertThrows(PedidoConflictoException.class,
                () -> service.crear("cliente@example.com", request(1L, 2)));
        assertEquals(1, producto.getStock());
    }

    @Test
    void cancelarPedidoPrevioAlDespachoRestauraStock() {
        Producto producto = producto(1L, 11_000, 3);
        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoPedido.RECIBIDO);
        pedido.setTotal(22_000L);
        pedido.setCorreoCliente("cliente@example.com");
        DetallePedido detalle = new DetallePedido();
        detalle.setProductoId(1L);
        detalle.setNombreProducto("Cilindro");
        detalle.setPrecioUnitario(11_000);
        detalle.setCantidad(2);
        detalle.setSubtotal(22_000L);
        pedido.agregarDetalle(detalle);
        when(pedidos.findById(7L)).thenReturn(Optional.of(pedido));
        when(productos.buscarPorIdParaActualizar(1L)).thenReturn(Optional.of(producto));

        var respuesta = service.actualizarEstado(7L, new ActualizarEstadoPedidoRequest(EstadoPedido.CANCELADO));

        assertEquals(EstadoPedido.CANCELADO, respuesta.estado());
        assertEquals(5, producto.getStock());
    }

    @Test
    void noPermiteQueUnClienteConsultePedidoAjeno() {
        Pedido pedido = new Pedido();
        pedido.setCorreoCliente("dueño@example.com");
        when(pedidos.findById(7L)).thenReturn(Optional.of(pedido));

        assertThrows(PedidoNoEncontradoException.class,
                () -> service.buscar(7L, "otro@example.com", false));
    }

    private CrearPedidoRequest request(Long productoId, int cantidad) {
        return new CrearPedidoRequest("Cliente", "912345678", "Calle 1",
                List.of(new LineaPedidoRequest(productoId, cantidad)));
    }

    private Producto producto(Long id, int precio, int stock) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setCodigo("CL001");
        producto.setNombre("Cilindro");
        producto.setPrecio(precio);
        producto.setStock(stock);
        return producto;
    }
}
