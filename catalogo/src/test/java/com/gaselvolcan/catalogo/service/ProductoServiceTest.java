package com.gaselvolcan.catalogo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gaselvolcan.catalogo.dto.ProductoRequest;
import com.gaselvolcan.catalogo.model.Producto;
import com.gaselvolcan.catalogo.repository.ProductoRepository;

class ProductoServiceTest {
    private ProductoRepository repository;
    private ProductoService service;

    @BeforeEach
    void preparar() {
        repository = mock(ProductoRepository.class);
        service = new ProductoService(repository);
        when(repository.save(any(Producto.class))).thenAnswer(invocacion -> invocacion.getArgument(0));
    }

    @Test
    void creaProductoConOfertaConsistente() {
        Producto producto = service.crear(new ProductoRequest("CL001", "Cilindro", "Gas", 11_000,
                12_000, 8, 5, true, "assets/cilindro.png", null));

        assertEquals("CL001", producto.getCodigo());
        assertEquals(8, producto.getDescuento());
        verify(repository).save(any(Producto.class));
    }

    @Test
    void rechazaDescuentoQueNoCoincideConLosPrecios() {
        assertThrows(IllegalArgumentException.class, () -> service.crear(new ProductoRequest(
                "CL001", "Cilindro", "Gas", 11_000, 12_000, 15, 5, true, "assets/cilindro.png", null)));
        verifyNoInteractions(repository);
    }

    @Test
    void rechazaPrecioAnteriorCuandoProductoNoEsOferta() {
        assertThrows(IllegalArgumentException.class, () -> service.crear(new ProductoRequest(
                null, "Cilindro", "Gas", 11_000, 12_000, null, 5, false, "assets/cilindro.png", null)));
        verifyNoInteractions(repository);
    }
}
