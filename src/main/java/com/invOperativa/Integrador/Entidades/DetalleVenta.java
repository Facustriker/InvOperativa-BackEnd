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
    private int subTotal;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "articulo")
    private Articulo articulo;
}
