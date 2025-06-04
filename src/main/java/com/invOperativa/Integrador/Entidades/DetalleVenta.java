package com.invOperativa.Integrador.Entidades;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "DetalleVenta")
public class DetalleVenta extends BaseEntity{

    @Column(name = "cant")
    private int cant;

    @Column(name = "subTotal")
    private float subTotal;

    @ManyToOne
    @JoinColumn(name = "articulo")
    private Articulo articulo;
}
