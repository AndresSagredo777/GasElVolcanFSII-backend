package com.gaselvolcan.catalogo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gaselvolcan.catalogo.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findAllByOrderByCreadoEnDesc();
    List<Pedido> findByCorreoClienteOrderByCreadoEnDesc(String correoCliente);
}
