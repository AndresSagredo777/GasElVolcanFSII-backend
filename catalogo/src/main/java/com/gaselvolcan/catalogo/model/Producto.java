package com.gaselvolcan.catalogo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(nullable = false)
    private Integer precio;

    @Column(name = "precio_anterior")
    private Integer precioAnterior;

    private Integer descuento;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private boolean oferta;

    @Column(nullable = false)
    private String imagen;

    @Column(length = 1000)
    private String descripcion;
}
