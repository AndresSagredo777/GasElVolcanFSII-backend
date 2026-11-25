package com.gaselvolcan.catalogo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gaselvolcan.catalogo.dto.ActualizarEstadoPedidoRequest;
import com.gaselvolcan.catalogo.dto.CrearPedidoRequest;
import com.gaselvolcan.catalogo.dto.PedidoResponse;
import com.gaselvolcan.catalogo.service.PedidoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse crear(Authentication autenticacion, @Valid @RequestBody CrearPedidoRequest request) {
        return service.crear(autenticacion.getName(), request);
    }

    @GetMapping("/mis-pedidos")
    public List<PedidoResponse> misPedidos(Authentication autenticacion) {
        return service.listarDelCliente(autenticacion.getName());
    }

    @GetMapping
    public List<PedidoResponse> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public PedidoResponse buscar(
            @PathVariable Long id,
            Authentication autenticacion) {
        boolean administrador = autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> autoridad.getAuthority().equals("ROLE_ADMIN"));
        return service.buscar(id, autenticacion.getName(), administrador);
    }

    @PatchMapping("/{id}/estado")
    public PedidoResponse actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoPedidoRequest request) {
        return service.actualizarEstado(id, request);
    }
}
