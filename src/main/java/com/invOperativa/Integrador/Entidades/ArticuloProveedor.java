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

    @Column(name = "costoPedido")
    private float costoPedido;

    @Column(name = "fhAsignacion", nullable = false)
    private Date fhAsignacion;

    @Column(name = "fechaBaja")
    private Date fechaBaja;

    @Column(name = "demoraEntrega")
    private int demoraEntrega;

    @Column(name = "isPredeterminado", nullable = false)
    private boolean isPredeterminado;

    @Column(name = "precioUnitario")
    private float costoUnitario;

    @Column(name = "nivelServicio")
    private float nivelServicio;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "articulo")
    private Articulo articulo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "proveedor")
    private Proveedor proveedor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "modeloInventario")
    private ModeloInventario modeloInventario;
}
