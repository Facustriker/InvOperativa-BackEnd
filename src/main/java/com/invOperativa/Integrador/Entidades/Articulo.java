package com.invOperativa.Integrador.Entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "Articulo")
public class Articulo extends BaseEntity{

    @Column(name = "descripcionArt")
    private String descripcionArt;

    @Column(name = "precioUnitario", nullable = false)
    private float precioUnitario;
}
