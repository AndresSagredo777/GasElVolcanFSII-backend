package com.gaselvolcan.catalogo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gaselvolcan.catalogo.dto.ProductoRequest;
import com.gaselvolcan.catalogo.model.Producto;
import com.gaselvolcan.catalogo.service.ProductoService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Producto> listar(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean oferta) {
        return service.listar(categoria, oferta);
    }

    @GetMapping("/{id}")
    public Producto buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Producto> crear(@Valid @RequestBody ProductoRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(datos));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public Producto actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest datos) {
        return service.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
