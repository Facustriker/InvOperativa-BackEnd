package com.invOperativa.Integrador.Entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "ArticuloProveedor")
public class ArticuloProveedor extends BaseEntity{

    @Column(name = "fhAsignacion", nullable = false)
    private Date fhAsignacion;

    @Column(name = "cargosPedido")
    private Float cargosPedido;

    @Column(name = "demoraEntrega")
    private Date demoraEntrega;

    @Column(name = "isPredeterminado", nullable = false)
    private boolean isPredeterminado;

    @Column(name = "precioUnitario")
    private double precioUnitario;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "articulo")
    private Articulo articulo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "proveedor")
    private Proveedor proveedor;
}
