package com.invOperativa.Integrador.Entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "OrdenCompra")
public class OrdenCompra extends BaseEntity{

    @Column(name = "fhAltaOrdenCompra", nullable = false)
    private Date fhAltaOrdenCompra;

    @Column(name = "fhBajaOrdenCompra")
    private Date fhBajaOrdenCompra;

    @Column(name = "isAuto")
    private Boolean isAuto;

    @Column(name = "total")
    private float total;


    @ManyToOne
    @JoinColumn(name = "estadoOrdenCompra")
    private EstadoOrdenCompra estadoOrdenCompra;

    @ManyToOne
    @JoinColumn(name = "proveedor")
    private Proveedor proveedor;

    @JoinColumn(name = "ordenCompraDetalles")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private Collection<OrdenCompraDetalle> ordenCompraDetalles = new ArrayList<>();

    public void addOrdenCompraDetalle(OrdenCompraDetalle ocd) {
        ordenCompraDetalles.add(ocd);
    }
}
