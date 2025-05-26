package com.invOperativa.Integrador.Entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "Articulo")
public class Articulo extends BaseEntity{

    @Column(name = "costoAlmacenamiento", nullable = false)
    private float costoAlmacenamiento;

    @Column(name = "descripcionArt", nullable = false)
    private String descripcionArt;

    @Column(name = "fhBajaArticulo")
    private Date fhBajaArticulo;

    @Column(name = "precioUnitario", nullable = false)
    private float precioUnitario;

    @Column(name = "inventarioMaxArticulo")
    private int inventarioMaxArticulo;

    @Column(name = "loteOptimo")
    private int loteOptimo;

    @Column(name = "proximaRevision")
    private Date proximaRevision;

    @Column(name = "puntoPedido")
    private int puntoPedido;

    @Column(name = "stock")
    private int stock;

    @Column(name = "stockSeguridad")
    private int stockSeguridad;

    @Column(name = "tiempoFijo")
    private int tiempoFijo;

    @Column(name = "nombre", nullable = false)
    private String nombre;

}
