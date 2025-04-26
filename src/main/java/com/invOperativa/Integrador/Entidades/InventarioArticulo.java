package com.invOperativa.Integrador.Entidades;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "InventarioArticulo")
public class InventarioArticulo extends BaseEntity{

    @Column(name = "inventarioMaxArticulo")
    private int inventarioMaxArticulo;

    @Column(name = "loteOptimo")
    private int loteOptimo;

    @Column(name = "puntoPedido")
    private int puntoPedido;

    @Column(name = "stock")
    private int stock;

    @Column(name = "stockSeguridad")
    private int stockSeguridad;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "articulo")
    private Articulo articulo;
}
