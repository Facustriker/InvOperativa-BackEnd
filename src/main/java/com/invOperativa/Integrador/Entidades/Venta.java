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
@Table(name = "Venta")
public class Venta extends BaseEntity{

    @Column(name = "montoTotal")
    private float montoTotal;

    @Column(name = "fhAltaVenta", nullable = false)
    private Date fhAltaVenta;

    @JoinColumn(name = "detalleVentas")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private Collection<DetalleVenta> detalleVentas = new ArrayList<>();

    public void addDetalleVenta(DetalleVenta dv) {
        detalleVentas.add(dv);
    }
}
