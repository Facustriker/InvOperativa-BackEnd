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

    @Column(name = "fhBajaOrdenCompra", nullable = false)
    private Date fhBajaOrdenCompra;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estadoOrdenCompra")
    private EstadoOrdenCompra estadoOrdenCompra;

    @JoinColumn(name = "ordenCompraDetalles")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private Collection<OrdenCompraDetalle> ordenCompraDetalles = new ArrayList<>();

    public void addOrdenCompraDetalle(OrdenCompraDetalle ocd) {
        ordenCompraDetalles.add(ocd);
    }
}
