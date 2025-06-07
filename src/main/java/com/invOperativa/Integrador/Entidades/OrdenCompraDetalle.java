package com.invOperativa.Integrador.Entidades;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "OrdenCompraDetalles")
public class OrdenCompraDetalle extends BaseEntity{

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "subTotal")
    private float subTotal;

    @ManyToOne
    @JoinColumn(name = "articuloProveedor")
    private ArticuloProveedor articuloProveedor;

}
