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
@Table(name = "Inventario")
public class Inventario extends BaseEntity{

    @Column(name = "fhBajaProveedor", nullable = false)
    private Date fhBajaInventario;

    @JoinColumn(name = "inventarioArticulos")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private Collection<InventarioArticulo> inventarioArticulos = new ArrayList<>();

    public void addInventarioArticulo(InventarioArticulo ia) {
        inventarioArticulos.add(ia);
    }
}
