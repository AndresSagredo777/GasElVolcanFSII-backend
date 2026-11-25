package com.gaselvolcan.catalogo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;

import com.gaselvolcan.catalogo.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findAllByOrderByIdAsc();

    List<Producto> findByCategoriaIgnoreCaseOrderByIdAsc(String categoria);

    List<Producto> findByOfertaOrderByIdAsc(boolean oferta);

    List<Producto> findByCategoriaIgnoreCaseAndOfertaOrderByIdAsc(String categoria, boolean oferta);

    @Query(value = "select * from productos where id = :id for update", nativeQuery = true)
    Optional<Producto> buscarPorIdParaActualizar(@Param("id") Long id);
}
