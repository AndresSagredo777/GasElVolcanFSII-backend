package com.gaselvolcan.catalogo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaselvolcan.catalogo.dto.ProductoRequest;
import com.gaselvolcan.catalogo.exception.ProductoNoEncontradoException;
import com.gaselvolcan.catalogo.model.Producto;
import com.gaselvolcan.catalogo.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Producto> listar(String categoria, Boolean oferta) {
        boolean tieneCategoria = categoria != null && !categoria.isBlank();

        if (tieneCategoria && oferta != null) {
            return repository.findByCategoriaIgnoreCaseAndOfertaOrderByIdAsc(categoria, oferta);
        }
        if (tieneCategoria) {
            return repository.findByCategoriaIgnoreCaseOrderByIdAsc(categoria);
        }
        if (oferta != null) {
            return repository.findByOfertaOrderByIdAsc(oferta);
        }
        return repository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public Producto buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }

    public Producto crear(ProductoRequest datos) {
        validarOferta(datos);
        Producto producto = new Producto();
        copiarDatos(producto, datos, false);
        return repository.save(producto);
    }

    public Producto actualizar(Long id, ProductoRequest datos) {
        validarOferta(datos);
        Producto producto = buscar(id);
        copiarDatos(producto, datos, true);
        return repository.save(producto);
    }

    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }

    private void copiarDatos(Producto producto, ProductoRequest datos, boolean esActualizacion) {
        if (!esActualizacion || datos.codigo() != null) {
            producto.setCodigo(datos.codigo() == null || datos.codigo().isBlank() ? null : datos.codigo().trim());
        }
        producto.setNombre(datos.nombre().trim());
        producto.setCategoria(datos.categoria().trim());
        producto.setPrecio(datos.precio());
        producto.setStock(datos.stock());
        producto.setOferta(datos.oferta());
        producto.setImagen(datos.imagen().trim());
        producto.setDescripcion(datos.descripcion() == null ? null : datos.descripcion().trim());

        producto.setPrecioAnterior(datos.precioAnterior());
        producto.setDescuento(datos.descuento());
    }

    private void validarOferta(ProductoRequest datos) {
        if (Boolean.TRUE.equals(datos.oferta())) {
            if (datos.precioAnterior() == null || datos.precioAnterior() <= datos.precio()) {
                throw new IllegalArgumentException("Una oferta requiere un precio anterior mayor al precio actual");
            }
            if (datos.descuento() == null) {
                throw new IllegalArgumentException("Una oferta requiere informar el descuento");
            }
            int porcentajeCalculado = (int) Math.round(
                    (datos.precioAnterior() - (double) datos.precio()) * 100 / datos.precioAnterior());
            if (datos.descuento() != porcentajeCalculado) {
                throw new IllegalArgumentException("El descuento no coincide con los precios informados");
            }
        } else if (datos.precioAnterior() != null || datos.descuento() != null) {
            throw new IllegalArgumentException("Sin oferta, precio anterior y descuento deben ser nulos");
        }
    }
}
